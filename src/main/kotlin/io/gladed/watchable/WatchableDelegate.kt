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
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private val channel: BroadcastChannel<C> by lazy {
        BroadcastChannel<C>(CAPACITY).also {
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

    /** Process [change], applying it to [owner]. */
    abstract fun onBoundChange(change: C)

    /** Binding, if any. */
    private var binding: Job? = null

    var boundTo: Watchable<T, C>? = null

    /** True if current processing a change from the watchable to which [owner] is bound. */
    private var isOnChange: Boolean = false

    /** Throw if this is not a good time to change the owner object. */
    private fun checkChange() {
        if (boundTo != null && !isOnChange) {
            throw IllegalStateException("A bound object may not be modified.")
        }
    }

    internal fun <U> applyIfOk(func: () -> U): U {
        checkChange()
        return func()
    }

    /**
     * Apply and deliver a change if it's safe to do so
     */
    fun <C2 : C> change(block: () -> C2): C2 {
        checkChange()
        return block().also { owner.launch { send(it) } }
    }

    /**
     * Same as change but allows that the block may not actually change anything
     */
    fun <C2 : C> changeOrNull(block: () -> C2?): C2? {
        checkChange()
        return block()?.also { owner.launch { send(it) } }
    }

    /** Deliver a change to watchers if possible. */
    fun deliver(change: C) {
        if (!channel.isClosedForSend) {
            launch {
                send(change)
            }
        }
    }

    /** Deliver a change to watchers if possible. */
    suspend fun send(change: C) {
        // Send, if we can
        if (!channel.isClosedForSend) {
            channel.send(change)
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
        binding = owner.watch(other) {
            isOnChange = true
            onBoundChange(it)
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

    fun watchOwner(scope: CoroutineScope, block: (C) -> Unit): Job {
        // Open first in case there are changes
        val sub = channel.openSubscription()
        val initial = initialChange
        return scope.launch {
            // Send a current copy of initial content
            block(initial)
            sub.consumeEach {
                block(it)
            }
        }
    }

    companion object {
        private const val CAPACITY = 2
    }
}
