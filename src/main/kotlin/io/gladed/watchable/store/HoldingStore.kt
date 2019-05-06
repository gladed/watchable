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
import kotlinx.coroutines.CoroutineScope
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
    private val createHold: suspend (T) -> Hold
) : CoroutineScope {
    override val coroutineContext = context + Job()

    private val map = mutableMapOf<String, MultiHold<Store<T>, T>>().guarded()

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
                    val dead = filterValues { it.release(newStore) }
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

    /** A [Store] whose objects are held when accessing them. */
    @UseExperimental(FlowPreview::class)
    private inner class SingleStore : Store<T> {
        override suspend fun put(key: String, value: T) {
            // First, test to see if there's a value present.
            try {
                back.get(key)
            } catch (c: Cannot) {
                // Kill the old hold if there was one, it's being replaced.
                map { remove(key) }?.stop()

                val newHold = createHold(value)
                // Handle create attempt
                newHold.onCreate()
                back.put(key, value)
                newHold.onStart()

                map {
                    put(key, MultiHold(this@SingleStore, value, newHold))
                }
                return
            }
            cannot("overwrite existing value")
        }

        @Suppress("TooGenericExceptionCaught") // Rollback in case of any failure
        override suspend fun get(key: String): T = try {
            hold(key) { back.get(key) }.first
        } catch (t: Throwable) {
            // On any failure remove this item from the global map.
            map { remove(key) }
            throw t
        }

        override suspend fun remove(key: String) {
            map { get(key) }?.remove()
            map { remove(key) }
            back.remove(key)
        }

        override fun keys(): Flow<String> = back.keys()

        private suspend fun hold(key: String, getValue: suspend () -> T): Pair<T, Hold> =
            map {
                get(key)?.also { it.reserve(this@SingleStore) } ?: MultiHold(this@SingleStore as Store<T>,
                    async(SupervisorJob()) {
                        val value = getValue()
                        val hold = createHold(value)
                        hold.onStart()
                        value to hold
                    }
                ).also {
                    put(key, it)
                }
            }.hold.await()
    }
}
