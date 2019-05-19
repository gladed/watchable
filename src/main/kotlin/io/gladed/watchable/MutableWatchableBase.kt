/*
 * (c) Copyright 2019 Glade Diviney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gladed.watchable

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.util.Guard
import kotlinx.coroutines.CoroutineScope

/** Base for implementing a type that is watchable, mutable, and bindable. */
@Suppress("TooManyFunctions") // Useful
internal abstract class MutableWatchableBase<T, M : T, C : Change> : WatchableBase<C>(), MutableWatchable<M, C> {

    /** The underlying mutable form of the data this object. When changes are applied, [changes] must be updated. */
    protected abstract val mutable: Guard<M>

    /** Copy a mutable [M] to an immutable [T]. */
    protected abstract fun M.toImmutable(): T

    /** Given the current state [T] return [C] representing the initial state. */
    protected abstract fun T.toInitialChange(): C

    /** Apply [change] to [M]. */
    protected abstract fun M.applyBoundChange(change: C)

    /** Collects changes applied during any mutation of [mutable]. */
    private val changes = mutableListOf<C>()

    /** The current immutable [T] form of [M]. */
    protected abstract var immutable: T

    /** The current binding if any. */
    private var binding: Binding? = null

    override val boundTo get() = binding?.other

    /** True when this object may be modified. */
    private var changesAllowed = true

    /** Clear the contents of the mutable form of the underlying data. */
    protected abstract fun M.erase()

    final override suspend fun clear() = invoke { erase() }

    override fun getInitialChange(): C? = immutable.toInitialChange()

    /** Record the latest change. */
    protected fun record(change: C) {
        @Suppress("UNCHECKED_CAST")
        val replace = (changes.lastOrNull() as? Addable<C>)?.plus(change)
        if (replace != null) {
            changes[changes.lastIndex] = replace
        } else {
            changes += change
        }
    }

    /** Run [func] if changes are currently allowed, or throw if not. */
    protected fun <U> doChange(func: () -> U): U =
        if (boundTo != null && (!changesAllowed && !isTwoWayBound())) {
            throw IllegalStateException("attempt to modify a bound object: ${isTwoWayBound()}")
        } else func()

    override suspend operator fun <U> invoke(func: M.() -> U): U =
        mutable {
            // Apply mutations
            func().also {
                if (changes.isNotEmpty()) {
                    val oldImmutable = immutable
                    // Recreate the immutable form now that the change is complete.
                    immutable = toImmutable()
                    // Deliver changes
                    val changeList = changes.toList()
                    changes.clear()

                    @Suppress("TooGenericExceptionCaught") // Required for roll-back behavior
                    try {
                        dispatch(changeList)
                    } catch (t: Throwable) {
                        // Roll-back and re-throw
                        immutable = oldImmutable
                        throw t
                    }
                }
            }
        }

    /** Wrapper for a binding. */
    private inner class Binding(val other: Watchable<*>, val watcher: Watcher) : Watcher {
        override suspend fun start() {
            watcher.start()
        }

        override fun cancel() {
            if (binding == this) unbind()
        }

        override suspend fun stop() {
            if (binding == this) {
                watcher.stop()
                unbind()
            }
        }
    }

    override fun bind(scope: CoroutineScope, origin: Watchable<C>) =
        bind(scope, origin) {
            applyBoundChange(it)
        }

    private fun isTwoWayBound() =
        (boundTo as? MutableWatchableBase<*, *, *>)?.let { it == boundTo && it.boundTo == this } == true

    override fun <C2 : Change> bind(
        scope: CoroutineScope,
        origin: Watchable<C2>,
        period: Long,
        apply: M.(C2) -> Unit
    ): Watcher {
        if (binding != null) throw IllegalStateException("object already bound")
        throwIfAlreadyBoundTo(origin)

        var mustErase = true

        // Start watching
        val job = origin.batch(scope, period) {
            invoke {
                changesAllowed = true
                if (mustErase) {
                    mustErase = false
                    erase()
                }
                for (change in it) {
                    apply(change)
                }
                changesAllowed = false
            }
        }

        // Store the binding
        return Binding(origin, job).also { binding = it }
    }

    override fun <M2, C2 : Change> twoWayBind(
        scope: CoroutineScope,
        other: MutableWatchable<M2, C2>,
        update: M.(C2) -> Unit,
        updateOther: M2.(C) -> Unit
    ): Watcher {
        val otherBase = other as MutableWatchableBase<*, M2, C2>
        if (binding != null || otherBase.binding != null) throw IllegalStateException("object already bound")

        var mustErase = true // First time behavior
        var bouncing = false // Prevent infinite bounce

        val fromOther = other.batch(scope, INLINE) {
            // Incoming from other must run first, copying content into this item.
            if (!bouncing) {
                bouncing = true
                invoke {
                    changesAllowed = true
                    if (mustErase) {
                        mustErase = false
                        erase()
                    }
                    for (change in it) update(change)
                    changesAllowed = false
                }
                bouncing = false
            }
        }

        return setBinding(other, batch(scope, INLINE) {
            // Also, copy changes from this item into other
            if (!bouncing) {
                bouncing = true
                with(other) {
                    invoke {
                        changesAllowed = true
                        for (change in it) updateOther(change)
                        changesAllowed = false
                    }
                }
                bouncing = false
            }
        }) + otherBase.setBinding(this, fromOther)
    }

    /** Provide access to [applyBoundChange] when T isn't known. */
    private fun doChange(mutable: M, change: C) {
        with(mutable) { applyBoundChange(change) }
    }

    /** Allow setting of [binding] when T isn't known. */
    private fun setBinding(other: Watchable<*>, watcher: Watcher) =
        Binding(other, watcher).also { binding = it }

    override fun twoWayBind(scope: CoroutineScope, other: MutableWatchable<M, C>): Watcher {
        val otherBase = other as MutableWatchableBase<*, M, C>
        return twoWayBind(scope, other, { applyBoundChange(it) }) {
            otherBase.doChange(this, it)
        }
    }

    private tailrec fun throwIfAlreadyBoundTo(other: Watchable<*>) {
        if (this === other) throw IllegalStateException("circular binding not permitted")
        // It's OK to bind to something that's two-way bound
        if ((other as? MutableWatchableBase<*, *, *>)?.isTwoWayBound() == true) return
        throwIfAlreadyBoundTo((other as? MutableWatchable<*, *>)?.boundTo ?: return)
    }

    override fun unbind() {
        if (isTwoWayBound()) {
            binding?.apply {
                watcher.cancel()
                binding = null
                (other as? MutableWatchableBase<*, *, *>)?.unbind()
            }
        } else {
            binding?.apply {
                watcher.cancel()
                binding = null
            }
        }
    }
}
