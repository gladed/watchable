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

import io.gladed.watchable.MapChange
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

class WatchableMapTest : ScopeTest() {
    @Rule @JvmField val changes = ChangeWatcherRule<MapChange<Int, String>>()

    @Test fun entries() {
        runBlocking {
            val map = watchableMapOf(1 to "1")
            map.use {
                val first = entries.first()
                assertEquals(first, first)
                log("Entry: $entries has hash ${entries.hashCode()}") // Coverage
                mustThrow(UnsupportedOperationException::class.java) {
                    entries.add(object : MutableMap.MutableEntry<Int, String> {
                        override val key: Int = 1
                        override val value: String = "1"
                        override fun setValue(newValue: String) = throw Error()
                    })
                }
                first.setValue("2")
            }
            assertEquals(mapOf(1 to "2"), map)
        }
    }

    @Test fun readOnly() {
        runBlocking {
            val map = watchableMapOf(1 to "1")
            val map2 = watchableMapOf(2 to "2")
            bind(map2, map)
            val map3 = map2.readOnly()
            watch(map3) { changes += it }
            assertThat(map.toString(), startsWith("WatchableMap("))
            assertThat(map3.toString(), startsWith("ReadOnlyWatchableMap("))
            changes.expect(MapChange.Initial(mapOf(2 to "2")))
            map.set(mapOf(3 to "3"))
            changes.expect(MapChange.Remove(2, "2"), MapChange.Add(1, "1"), MapChange.Remove(1, "1"),
                MapChange.Add(3, "3"))
        }
    }

    @Test fun bindReadOnly() {
        runBlocking {
            val map = watchableMapOf(1 to "1")
            val map2 = watchableMapOf(2 to "2")
            val map3 = map.readOnly()
            bind(map2, map3)
            map.use {
                put(3, "3")
            }

            map3.watchUntil(this) {
                assertEquals(map, map3)
            }
        }
    }

    @Test fun listApis() {
        val map = watchableMapOf(1 to "1", 2 to "2")
        assertEquals(2, map.size)
        assertEquals("1", map.entries.first().value)
        assertEquals(1, map.keys.first())
        assertEquals("1", map.values.first())
        assertTrue(map.containsKey(2))
        assertTrue(map.containsValue("1"))
        assertEquals("2", map[2])
        assertFalse(map.isEmpty())
    }
}
