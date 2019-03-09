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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Duration

/** Base for implementing a type that is watchable, mutable, and bindable. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@Suppress("TooManyFunctions") // Useful
abstract class MutableWatchableBase<T, M : T, C : Change<T>> : MutableWatchable<T, M, C> {

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
    private val broadcaster: BroadcastChannel<List<C>> by lazy {
        BroadcastChannel<List<C>>(CAPACITY).apply {
            coroutineContext[Job]?.invokeOnCompletion {
                close()
            }
        }
    }

    /** Collects changes applied during any mutation of [mutable]. */
    protected val changes = mutableListOf<C>()

    /** The latest immutable [T] form of [M] if currently known. */
    private var immutable: T? = null

    /** The mutex protecting to [mutable]. */
    private val mutableMutex = Mutex()

    /** The current binding if any. */
    private var binding: Binding? = null

    override val boundTo get() = binding?.other

    /** True if current processing a change from the watchable to which this object is bound. */
    private var isOnBoundChange: Boolean = false

    fun CoroutineScope.subscribe(): ReceiveChannel<List<C>> {
        if (!isActive) throw IllegalStateException("Cannot watch an inactive watchable")

        val channel = Channel<List<C>>(CAPACITY)
        // Create a child scope unrelated to the caller's
        val childScope = CoroutineScope(coroutineContext)
        childScope.launch {
            // Simultaneously open the subscription and get the initial change set.
            val (initial, subscription) = mutableMutex.withLock {
                (immutable ?: mutable.toImmutable().also { immutable = it }).toInitialChange() to
                    broadcaster.openSubscription()
            }
            channel.send(listOf(initial))

            // Launch a new job into the caller's coroutine context, but don't block it up forever.
            val consumerJob = CoroutineScope(coroutineContext + SupervisorJob()).launch {
                subscription.consumeEach {
                    if (this@MutableWatchableBase.isActive && this@subscribe.isActive) {
                        channel.send(it)
                    } else {
                        // Ignore any leftovers and cancel
                        cancel()
                    }
                }
            }
            this@MutableWatchableBase.coroutineContext[Job]!!.invokeOnCompletion {
                consumerJob.cancel()
            }
        }
        return channel
    }

    override fun CoroutineScope.watchBatches(minPeriod: Duration, func: suspend (List<C>) -> Unit): Job {
        if (!isActive) throw IllegalStateException("Cannot watch an inactive watchable")

        // Create our own job handle so we can manage cancellation independently of the caller
        val batchJob = Job()
        val childScope = CoroutineScope(coroutineContext + batchJob)
        childScope.launch {
            // Simultaneously open the subscription and get the initial change set.
            val (initial, subscription) = mutableMutex.withLock {
                (immutable ?: mutable.toImmutable().also { immutable = it }).toInitialChange() to
                    broadcaster.openSubscription()
            }

            // Deliver initial NOW and follow up with all subsequent changes.
            func(listOf(initial))

            // Launch a new job into the caller's coroutine context, but don't block it up forever.
            val consumerJob = CoroutineScope(coroutineContext + SupervisorJob()).launch {
                val batchedSubscription = batch(subscription, minPeriod.toMillis())
                batchedSubscription.consumeEach {
                    if (this@MutableWatchableBase.isActive && this@watchBatches.isActive) {
                        func(it)
                    } else {
                        // Ignore any leftovers and cancel
                        cancel()
                    }
                }
            }

            // If the batchJob dies then kill the consumer job.
            batchJob.invokeOnCompletion { consumerJob.cancel() }
            this@MutableWatchableBase.coroutineContext[Job]!!.invokeOnCompletion {
                consumerJob.cancel()
            }
        }
        return batchJob
    }

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

    override suspend fun <U> use(func: M.() -> U): U =
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
            // There's a massive performance benefit here because we don't have
            // to make and throw away a lot of near-identical copies.
            immutable = null
            // Deliver and clear out changes if the channel is open
            val changeList = changes.toList()
            changes.clear()
            if (isActive) {
                broadcaster.send(changeList)
            }
        }
    }

    /** Wrapper for a binding. */
    private class Binding(val other: Watchable<*, *>, val job: Job)

    override fun bind(origin: Watchable<T, C>) {
        bind(origin) {
            applyBoundChange(it)
        }
    }

    override fun <T2, C2 : Change<T2>> bind(origin: Watchable<T2, C2>, apply: M.(C2) -> Unit) {
        if (binding != null) throw IllegalStateException("Object already bound")

        // Chase up the parent stack to make sure it's not circular
        var parent = origin as? MutableWatchable<*, *, *>
        while (parent != null) {
            if (parent === this) throw IllegalStateException("Circular binding not permitted")
            parent = parent.boundTo as? MutableWatchable<*, *, *>
        }

        // Start watching
        val job = watchBatches(origin) {
            use {
                isOnBoundChange = true
                for (change in it) {
                    apply(change)
                    if (!isActive) break
                }
                isOnBoundChange = false
            }
        }

        // Store the binding
        binding = Binding(origin, job)
    }

    override fun unbind() {
        binding?.apply {
            job.cancel()
            binding = null
        }
    }

    companion object {
        private const val CAPACITY = 10
    }
}
