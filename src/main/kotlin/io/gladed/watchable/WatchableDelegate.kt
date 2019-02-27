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
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext

/** Common internal implementations for watchable + bindable functions. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
abstract class WatchableDelegate<T, C : Change<T>>(
    override val coroutineContext: CoroutineContext,
    private val owner: Watchable<T, C>
) : CoroutineScope {

    /** The internal channel used to broadcast changes to watchers. */
    private val channel: BroadcastChannel<List<C>> by lazy {
        BroadcastChannel<List<C>>(CAPACITY).also {
            CoroutineScope(coroutineContext).launch {
                // Keep channel open until the context is closed, for any reason.
                delay(Long.MAX_VALUE)
            }.invokeOnCompletion {
                channel.close()
            }
        }
    }

    /** A change reflecting the current value of this collection. (New watchers receive this). */
    abstract val initialChange: C

    /** Process [changes], applying them to [owner]. */
    abstract fun onBoundChanges(changes: List<C>)

    /** Binding, if any. */
    private var binding: Job? = null

    var boundTo: Watchable<T, C>? = null

    /** True if current processing a change from the watchable to which [owner] is bound. */
    private var isOnChange: Boolean = false

    /**
     * Throw if this is not a good time to mutate the owner object (because it's bound and not currently
     * processing binding-related changes).
     */
    fun checkChange() {
        if (boundTo != null && !isOnChange) {
            throw IllegalStateException("A bound object may not be modified.")
        }
    }

    // Note: remove change and changeOrNull when other watchables are refactored
    /**
     * Apply and deliver a change if it's safe to do so
     */
    fun <C2 : C> change(block: () -> C2): C2 {
        checkChange()
        return block().also { owner.launch { send(listOf(it)) } }
    }

    /**
     * Same as change but allows that the block may not actually change anything
     */
    fun <C2 : C> changeOrNull(block: () -> C2?): C2? {
        checkChange()
        return block()?.also { owner.launch { send(listOf(it)) } }
    }

    /** Deliver changes to watchers if possible. */
    suspend fun send(changes: List<C>) {
        // Send, if we can
        if (!channel.isClosedForSend) {
            channel.send(changes)
        }
    }

    fun bind(other: Watchable<T, C>) {
        if (binding != null) throw IllegalStateException("Object already bound")

        // Chase up the parent stack
        var parent = other as? Bindable<*, *>
        while (parent != null) {
            if (parent === owner) throw IllegalStateException("Circular binding not permitted")
            parent = parent.boundTo as? Bindable<*, *>
        }

        boundTo = other

        // Perform the binding on owner's scope
        binding = owner.watchBatches(other) {
            isOnChange = true
            onBoundChanges(it)
            isOnChange = false
        }
    }

    fun unbind() {
        binding?.also {
            it.cancel()
            binding = null
            boundTo = null
        }
    }

    fun watchOwnerBatch(scope: CoroutineScope, block: (List<C>) -> Unit): Job {
        // Open first in case there are changes
        val sub = channel.openSubscription()
        val initial = initialChange
        return scope.launch {
            // Send initial content
            block(listOf(initial))
            // Batch received events for a measurable performance benefit
            sub.consumeBatched { changes ->
                block(changes)
                yield()
            }
        }
    }

    /** Like consumeEach, but instead of handling each item, try to get a bunch of them and pass them along. */
    private suspend inline fun <T : Any> ReceiveChannel<List<T>>.consumeBatched(
        handleItems: (List<T>) -> Unit
    ) {
        val buffer = mutableListOf<T>()
        while (true) {
            receiveOrNull()?.also { buffer.addAll(it) } ?: break
            for (x in 2..CAPACITY) {
                poll()?.also { buffer.addAll(it) } ?: break
            }
            handleItems(buffer)
            buffer.clear()
        }
    }

    companion object {
        private const val CAPACITY = 20
    }
}
