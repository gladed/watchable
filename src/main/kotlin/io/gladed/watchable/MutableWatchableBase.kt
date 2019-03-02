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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.coroutineContext

/** Base for implementing a type that is watchable, mutable, and bindable. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
abstract class MutableWatchableBase<M: T, T, C : Change<T>> : MutableWatchable<M, T, C> {

    /** The underlying mutable form of the data this object. When changes are applied, [changes] must be updated. */
    protected abstract val mutable: M

    /** Copy a mutable [M] to an immutable [T]. */
    protected abstract fun M.toImmutable(): T

    /** Given the current state T return a C representing that initial state. */
    protected abstract fun T.toInitialChange(): C

    /** Apply all [changes] to [M]. */
    protected abstract fun M.applyBoundChange(change: C)

    /** Completely replace the contents with a new value, updating changes() as required. */
    protected abstract fun replace(newValue: T)

    /** The internal channel used to broadcast changes to watchers. */
    private val channel: BroadcastChannel<List<C>> by lazy {
        BroadcastChannel<List<C>>(CAPACITY).also {
            launch {
                // Keep channel open until the enclosing scope completes
                delay(Long.MAX_VALUE)
            }.invokeOnCompletion {
                channel.close()
            }
        }
    }

    /** Collects changes applied during any mutation of [mutable]. */
    protected val changes = mutableListOf<C>()

    /** The latest immutable [T] form of [M] if currently known. */
    private var immutable: T? = null

    /** The mutex protecting to [mutable]. */
    private val mutableMutex = Mutex()

    /** Run [func] if changes are currently allowed on [immutable], or throw if not. */
    protected fun <U> doChange(func: () -> U): U =
        if (boundTo != null && !isOnBoundChange) {
            throw IllegalStateException("A bound object may not be modified.")
        } else func()

    override suspend fun get(): T = immutable.let {
        it ?: mutableMutex.withLock {
            immutable = mutable.toImmutable()
            immutable!!
        }
    }

    override suspend fun <U> use(func: suspend M.() -> U): U =
        mutableMutex.withLock {
            // Apply mutations
            mutable.func().also {
                deliverChanges()
            }
        }

    override suspend fun set(value: T) {
        mutableMutex.withLock {
            doChange {
                replace(value)
            }
            deliverChanges()
        }
    }

    private suspend fun deliverChanges() {
        if (changes.isNotEmpty()) {
            // Clear immutable because it's now wrong
            immutable = null
            // Deliver and clear out changes if the channel is open
            val changeList = changes.toList()
            changes.clear()
            if (isActive) {
                channel.send(changeList)
            }
        }
    }

    override fun watchBatches(scope: CoroutineScope, func: suspend (List<C>) -> Unit): Job {
        if (!isActive) throw IllegalStateException("Cannot watch an inactive watchable")
        return scope.launch {
            // Simultaneously open the subscription and get the initial change set.
            val (initial, subscription) = mutableMutex.withLock {
                (immutable ?: mutable.toImmutable().also { immutable = it }).toInitialChange() to
                    channel.openSubscription()
            }

            // Deliver initial NOW and follow up with all subsequent changes.
            func(listOf(initial))
            subscription.consumeBatched(func)
        }
    }

    /** The current binding if any. */
    private var binding: Binding? = null

    /** Wrapper for a binding. */
    private class Binding(val other: Watchable<*, *>, val job: Job)

    /** True if current processing a change from the watchable to which this object is bound. */
    private var isOnBoundChange: Boolean = false

    override val boundTo get() = binding?.other

    override fun bind(scope: CoroutineScope, other: Watchable<T, C>) {
        if (binding != null) throw IllegalStateException("Object already bound")

        // Chase up the parent stack to make sure it's not circular
        var parent = other as? Bindable<*, *>
        while (parent != null) {
            if (parent === this) throw IllegalStateException("Circular binding not permitted")
            parent = parent.boundTo as? Bindable<*, *>
        }

        // Start watching
        val job = other.watchBatches(scope) {
            use {
                isOnBoundChange = true
                for (change in it) {
                    applyBoundChange(change)
                    if (!isActive) break
                }
                isOnBoundChange = false
            }
        }

        // Store the binding
        binding = Binding(other, job)
    }

    override fun unbind() {
        binding?.apply {
            job.cancel()
            binding = null
        }
    }

    companion object {
        private const val CAPACITY = 20

        /** Like consumeEach, but instead of handling each item, try to get a bunch of them and pass them along. */
        private suspend fun <T : Any> ReceiveChannel<List<T>>.consumeBatched(
            handleItems: suspend (List<T>) -> Unit
        ) {
            val buffer = mutableListOf<T>()
            while (coroutineContext.isActive) {
                receiveOrNull()?.also { buffer.addAll(it) } ?: break
                for (x in 2..CAPACITY) {
                    poll()?.also { buffer.addAll(it) } ?: break
                }
                if (coroutineContext.isActive) {
                    println("Handling $buffer")
                    handleItems(buffer.toList())
                }
                buffer.clear()
            }
        }
    }
}
