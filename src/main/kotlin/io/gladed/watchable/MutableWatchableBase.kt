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

import io.gladed.watchable.util.Guard
import kotlinx.coroutines.CoroutineScope

/** Base for implementing a type that is watchable, mutable, and bindable. */
@Suppress("TooManyFunctions") // Useful
internal abstract class MutableWatchableBase<T, V, M : T, C : Change> : WatchableBase<C>(), MutableWatchable<M, C> {

    /** The underlying mutable form of the data this object. When changes are applied, [changes] must be updated. */
    protected abstract val mutable: Guard<M>

    /** Copy a mutable [M] to an immutable [T]. */
    protected abstract fun M.toImmutable(): T

    /** Given the current state [T] return [C] representing the initial state, if any. */
    protected abstract fun T.toInitialChange(): C?

    /** Apply [change] to [M]. */
    protected abstract fun M.applyBoundChange(change: C)

    /** Collects changes applied during any mutation of [mutable]. */
    private val changes = mutableListOf<C>()

    /** The current immutable [T] form of [M]. */
    protected abstract var immutable: T

    /** The current binding if any. */
    private var binding: Binding? = null

    override val boundTo get() = binding?.other

    /** True if current processing a change from the watchable to which this object is bound. */
    private var isOnBoundChange: Boolean = false

    /** Clear the contents of the mutable form of the underlying data. */
    protected abstract fun M.erase()

    final override suspend fun clear() = invoke { erase() }

    override fun getInitialChange(): C? =
        immutable.toInitialChange()

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
        if (boundTo != null && !isOnBoundChange) {
            throw IllegalStateException("A bound object may not be modified.")
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

    override fun <C2 : Change> bind(
        scope: CoroutineScope,
        origin: Watchable<C2>,
        period: Long,
        apply: M.(C2) -> Unit
    ): Watcher {
        if (binding != null) throw IllegalStateException("Object already bound")

        throwIfAlreadyBoundTo(origin)

        var uncleared = true

        // Start watching
        val job = origin.batch(scope, period) {
            invoke {
                isOnBoundChange = true
                if (uncleared) {
                    uncleared = false
                    erase()
                }
                for (change in it) {
                    apply(change)
                }
                isOnBoundChange = false
            }
        }

        // Store the binding
        return Binding(origin, job).also { binding = it }
    }

    private tailrec fun throwIfAlreadyBoundTo(other: Watchable<*>) {
        if (this === other) throw IllegalStateException("Circular binding not permitted")
        throwIfAlreadyBoundTo((other as? MutableWatchable<*, *>)?.boundTo ?: return)
    }

    override fun unbind() {
        binding?.apply {
            watcher.cancel()
            binding = null
        }
    }
}
