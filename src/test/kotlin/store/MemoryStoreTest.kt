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

package store

import impossible
import io.gladed.watchable.store.MemoryStore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Test
import runTest

@UseExperimental(FlowPreview::class)
class MemoryStoreTest {
    private val store = MemoryStore<Thing>("thing")
    private val thing = Thing(value = 1)

    @Test fun `put and get`() = runTest {
        store.put(thing.id, thing)
        assertEquals(thing, store.get(thing.id))
    }

    @Test fun `get all keys`() = runTest {
        store.put(thing.id, thing)
        assertEquals(listOf(thing.id), store.keys().toList())
    }

    @Test fun `remove by id`() = runTest {
        store.put(thing.id, thing)
        store.remove(thing.id)
        impossible {
            store.get(thing.id)
        }
    }
}
