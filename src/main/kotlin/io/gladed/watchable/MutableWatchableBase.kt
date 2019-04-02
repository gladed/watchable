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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.selects.whileSelect
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.yield

/** Base for implementing a type that is watchable, mutable, and bindable. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@Suppress("TooManyFunctions") // Useful
abstract class MutableWatchableBase<T, V, M : T, C : Change> : MutableWatchable<T, V, M, C> {

    /** The underlying mutable form of the data this object. When changes are applied, [changes] must be updated. */
    protected abstract val mutable: M

    /** The mutex protecting [mutable]. */
    private val mutableMutex = Mutex()

    /** Copy a mutable [M] to an immutable [T]. */
    protected abstract fun M.toImmutable(): T

    /** Given the current state T return a C representing that initial state, if any. */
    protected abstract fun T.toInitialChange(): C?

    /** Apply all [changes] to [M]. */
    protected abstract fun M.applyBoundChange(change: C)

    /** The internal channel used to broadcast changes to watchers. */
    private val broadcaster: BroadcastChannel<List<C>> by lazy {
        BroadcastChannel<List<C>>(CAPACITY)
    }

    /** Collects changes applied during any mutation of [mutable]. */
    protected val changes = mutableListOf<C>()

    /** The latest immutable [T] form of [M] when known. */
    private var immutable: T? = null

    override val value: T get() = immutable ?: mutable.toImmutable().also { immutable = it }

    /** The current binding if any. */
    private var binding: Binding? = null

    override val boundTo get() = binding?.other

    /** True if current processing a change from the watchable to which this object is bound. */
    private var isOnBoundChange: Boolean = false

    @Suppress("UNUSED_VARIABLE")
    override fun batch(
        scope: CoroutineScope,
        minPeriod: Long,
        func: suspend (List<C>) -> Unit
    ) = scope.operate { closeMutex ->
        // Simultaneously get first change and an upstream subscription
        val (initial, subscription) = mutableMutex.withLock {
            value.toInitialChange() to broadcaster.openSubscription()
        }

        // Batch the subscription contents
        val rxChannel = collect(subscription, minPeriod, listOfNotNull(initial))
        whileSelect {
            rxChannel.onReceiveOrNull {
                if (it != null) {
                    func(it)
                    true
                } else {
                    false
                }
            }
            closeMutex.onLock {
                // Cancel upstream of rxChannel
                subscription.cancel()
                yield()
                true
            }
        }
    }

    /** Run [func] if changes are currently allowed on [immutable], or throw if not. */
    protected fun <U> doChange(func: () -> U): U =
        if (boundTo != null && !isOnBoundChange) {
            throw IllegalStateException("A bound object may not be modified.")
        } else func()

    override suspend fun <U> use(func: M.() -> U): U =
        mutableMutex.withLock {
            // Apply mutations
            mutable.func().also {
                deliverChanges()
            }
        }

    /** Push outstanding changes, if any, into the broadcast channel. */
    private suspend fun deliverChanges() {
        if (changes.isNotEmpty()) {
            // Clear the immutable form, allow it to be repopulated later
            immutable = mutable.toImmutable()
            // Deliver and clear out changes if the channel is open
            val changeList = changes.toList()
            changes.clear()
            broadcaster.send(changeList)
        }
    }

    /** Wrapper for a binding. */
    private class Binding(val other: Watchable<*, *, *>, val handle: WatchHandle)

    override suspend fun bind(scope: CoroutineScope, origin: Watchable<T, V, C>) {
        bind(scope, origin) {
            applyBoundChange(it)
        }
    }

    override suspend fun <T2, V2, C2 : Change> bind(
        scope: CoroutineScope,
        origin: Watchable<T2, V2, C2>,
        apply: M.(C2) -> Unit
    ) {
        if (binding != null) throw IllegalStateException("Object already bound")

        throwIfAlreadyBoundTo(origin)

        clear()

        // Start watching
        val job = origin.batch(scope) {
            use {
                isOnBoundChange = true
                for (change in it) {
                    apply(change)
                }
                isOnBoundChange = false
            }
        }

        // Store the binding
        binding = Binding(origin, job)
    }

    private tailrec fun throwIfAlreadyBoundTo(other: Watchable<*, *, *>) {
        if (this === other) throw IllegalStateException("Circular binding not permitted")
        throwIfAlreadyBoundTo((other as? MutableWatchable<*, *, *, *>)?.boundTo ?: return)
    }

    override fun unbind() {
        binding?.apply {
            handle.cancel()
            binding = null
        }
    }

    companion object {
        const val CAPACITY = 10
    }
}
