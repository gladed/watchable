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

import io.gladed.watchable.util.guard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

/** A RAM cache that prevents overuse of backing store by serving objects it already has loaded. */
class Cache<T : Any>(
    context: CoroutineContext,
    private val back: Store<T>
) : Store<T>, CoroutineScope {
    override val coroutineContext = context + Job()

    private val finding = mutableMapOf<String, Deferred<T>>().guard()
    private val found = mutableMapOf<String, WeakReference<T>>().guard()

    override suspend fun get(key: String): T =
        found {
            clearDead()
            get(key)?.get()
        } ?: finding {
            startGetting(key)
        }.await()

    private fun MutableMap<String, Deferred<T>>.startGetting(key: String): Deferred<T> =
        getOrPut(key) {
            // Do this asynchronously, but do not allow its failure to bring down [Cache] context
            async(coroutineContext + SupervisorJob()) {
                val result = back.get(key)
                if (quashFind(key)) {
                    // Only record the result if we quashed the find
                    found { put(key, WeakReference(result)) }
                }
                result
            }
        }

    override suspend fun put(key: String, value: T) {
        quashFind(key) // Outstanding gets should not update cache
        found {
            clearDead()
            put(key, WeakReference(value))
        }
        back.put(key, value)
    }

    /** Eliminate unused refs. */
    private fun MutableMap<String, WeakReference<T>>.clearDead() {
        entries.removeIf { it.value.get() == null }
    }

    override suspend fun remove(key: String) {
        quashFind(key) // Outstanding gets should not update cache
        found { remove(key) }
        back.remove(key)
    }

    private suspend fun quashFind(key: String): Boolean =
        finding { remove(key) } != null

    override fun keys() = back.keys()
}
