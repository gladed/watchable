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

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/** An object that retrieves elements by key. */
@UseExperimental(FlowPreview::class)
interface Store<T : Any> {
    /** Return the corresponding element, or throw if not present. */
    suspend fun get(key: String): T

    /** Write something to the store at the given key, overwriting what was there, if anything. */
    suspend fun put(key: String, value: T)

    /** Delete any data found at [key]. */
    suspend fun delete(key: String)

    /** Return a flow of all keys present in the store. */
    fun keys(): Flow<String>

    /** Convert this [Store] of deflated items into a [Store] of inflated items [U]. */
    fun <U : Any> inflate(inflater: Inflater<T, U>): Store<U> = object : Store<U> {
        override suspend fun get(key: String): U =
            inflater.inflate(this@Store.get(key))

        override suspend fun put(key: String, value: U) {
            this@Store.put(key, inflater.deflate(value))
        }

        override suspend fun delete(key: String) {
            this@Store.delete(key)
        }

        override fun keys() = this@Store.keys()
    }
}
