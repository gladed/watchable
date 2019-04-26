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

package io.gladed.watchable.store

import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A [Store] factory producing stores that trigger operations on its items while those objects are in use.
 *
 * For any new object retrieved, [createHold] is called to construct a [Hold] on the object.
 */
class HoldingStore<T : Any>(
    /** The parent context for starting and stopping operations. */
    context: CoroutineContext,
    /** The store being wrapped. */
    val back: Store<T>,
    private val createHold: suspend T.() -> Hold
) : CoroutineScope {
    override val coroutineContext = context + Job()

    private val map = mutableMapOf<String, Holding<T>>().guarded()

    /**
     * Return a new [Store]; items accessed by this store will have a corresponding hold (see [createHold]) in
     * effect until the completion of all scopes using the item.
     */
    fun create(scope: CoroutineScope): Store<T> {
        val newStore = SingleStore()
        scope.coroutineContext[Job]?.invokeOnCompletion {
            launch {
                map {
                    // Yank this store from all holds
                    val dead = filterValues { it.remove(newStore) }
                    // Yank all newly stopped holds
                    keys.removeAll(dead.keys)
                }
            }
        }
        return newStore
    }

    /** Release everything regardless of the state of scopes. */
    suspend fun stop() {
        map { toMap().also { clear() } }.values
            .forEach { it.stop() }
    }

    /** Attempt to hold an instance of [T] on behalf of one or more [Store]s. */
    private class Holding<T : Any>(first: Store<T>, val hold: Deferred<Pair<T, Hold>>) {
        private val stores = mutableSetOf(first).guarded()

        suspend fun add(store: Store<T>) {
            stores { add(store) }
        }

        /** Removes a store and returns true if this object can be discarded (e.g. no more stores). */
        suspend fun remove(store: Store<T>) =
            if (stores { remove(store); isEmpty() }) {
                stop()
                true
            } else {
                false
            }

        /** Stop holding as we delete this item. */
        suspend fun delete() {
            val toStop = hold.await().second
            toStop.remove()
            toStop.stop()
        }

        /** Stop holding. */
        suspend fun stop() {
            // If the request isn't done then cancel it
            hold.cancel()
            @Suppress("EmptyCatchBlock") // Ignore cancellations
            try {
                // Stop watching if not already cancelled
                if (!hold.isCancelled) {
                    hold.await().second.stop()
                }
            } catch (c: CancellationException) { }
        }
    }

    /** A [Store] whose objects are held when accessing them. */
    @UseExperimental(FlowPreview::class)
    private inner class SingleStore : Store<T> {
        override suspend fun put(key: String, value: T) {
            // Put a hold on the value
            hold(key) { value }.apply {
                if (first !== value) {
                    cannot("replace while different object with same key is in use")
                }
            }
            // Put it in the backing store
            back.put(key, value)
        }

        @Suppress("TooGenericExceptionCaught") // Rollback in case of any failure
        override suspend fun get(key: String): T = try {
            hold(key) { back.get(key) }.first
        } catch (t: Throwable) {
            // On any failure remove this item from the global map.
            map { remove(key) }
            throw t
        }

        override suspend fun delete(key: String) {
            map { get(key) }?.delete()
            map { remove(key) }
            back.delete(key)
        }

        override fun keys(): Flow<String> = back.keys()

        private suspend fun hold(key: String, getValue: suspend () -> T): Pair<T, Hold> =
            map {
                get(key)?.also { it.add(this@SingleStore) }
                    ?: Holding(this@SingleStore, async(SupervisorJob()) {
                        val value = getValue()
                        val hold = value.createHold()
                        hold.start()
                        value to hold
                    }).also {
                        put(key, it)
                    }
            }.hold.await()
    }
}
