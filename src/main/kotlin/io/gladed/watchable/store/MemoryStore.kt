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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

/** A store entirely in RAM. */
@UseExperimental(FlowPreview::class)
class MemoryStore<T : Any>(private val name: String) : Store<T> {

    private val map = mutableMapOf<String, T>().guard()

    override suspend fun get(key: String): T =
        map { get(key) ?: cannot("get $name by key") }

    override suspend fun put(key: String, value: T) {
        map { put(key, value) }
    }

    override suspend fun remove(key: String) {
        map { remove(key) }
    }

    /** Return the keys available in memory at the moment the flow is first consumed. */
    override fun keys() = listOf(map).asFlow().flatMapConcat {
        map { keys.toSet() }.asFlow()
    }
}
