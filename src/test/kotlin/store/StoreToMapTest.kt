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

import io.gladed.watchable.Change
import io.gladed.watchable.Watchable
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.store.Container
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.toWatchableMap
import io.gladed.watchable.toWatchableValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
class StoreToMapTest {
    data class Thing(val id: String = UUID.randomUUID().toString(), val value: Int, val name: WatchableValue<String>) : Container {
        override val watchables: Watchable<Change> = name
    }

    private val store = mockk<Store<Thing>>(relaxUnitFun = true)
    private val thing = Thing(value = 1, name = "one".toWatchableValue())

    @Before fun setup() {
        coEvery { store.keys() } coAnswers { listOf(thing.id).asFlow() }
        coEvery { store.get(thing.id) } returns thing
    }

    @Test fun `load initial values`() = runBlockingTest {
        val map = store.toWatchableMap(this, 50)
        assertEquals(thing, map[thing.id])
    }

    @Test fun `put when new contents are stored`() {
        runBlockingTest {
            val map = store.toWatchableMap(this, 50)
            map { put(thing.id, thing.copy(value = 2)) }

        }
        coVerify { store.put(thing.id, thing.copy(value = 2)) }
    }

    @Test fun `remove when items are removed`() {
        runBlockingTest {
            val map = store.toWatchableMap(this, 50)
            map.remove(thing.id)
        }
        coVerify { store.remove(thing.id) }
    }

    @Test fun `put when contents of container items change`() {
        runBlockingTest {
            store.toWatchableMap(this, 50)
            thing.name.set("One")
        }
        coVerify { store.put(thing.id, thing) }
    }
}
