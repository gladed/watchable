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
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.watch
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class WatchableMapTest {
    private val changes = mutableListOf<MapChange<Int, String>>()

    @Test fun add() {
        runThenCancel {
            val map = watchableMapOf<Int, String>()
            watch(map) {
                log("Receive $it")
                changes += it
            }
            map.putAll(mapOf(6 to "6", 5 to "5"))
            map[6] = "6" // No-op
            Assert.assertThat(map.toString(), CoreMatchers.startsWith("WatchableMap("))
            yield()
            yield()
        }

        assertEquals(MapChange.Initial(emptyMap<Int, String>()), changes[0])
        assertEquals(MapChange.Add(6, "6"), changes[1])
        assertEquals(MapChange.Add(5, "5"), changes[2])
        assertEquals(3, changes.size)
    }

    @Test fun noEntryAdd() {
        try {
            runThenCancel {
                val map = watchableMapOf<Int, String>()
                log("Attempting add")
                map.entries.add(object : MutableMap.MutableEntry<Int, String> {
                    override var key: Int = 5
                    override var value: String = "5"
                    override fun setValue(newValue: String): String {
                        return value.also { value = "6" }
                    }
                })
                fail("Shouldn't get here")
            }
        } catch (e: UnsupportedOperationException) { }
    }

    @Test fun remove() {
        runThenCancel {
            val map = watchableMapOf(5 to "5")
            watch(map) {
                log("Receive $it")
                changes += it
            }
            assertEquals("5", map.remove(5))
            yield()
            yield()
        }

        assertEquals(MapChange.Initial(mapOf(5 to "5")), changes[0])
        assertEquals(MapChange.Remove(5, "5"), changes[1])
    }

    @Test fun removeByEntry() {
        runThenCancel {
            val map = watchableMapOf(5 to "5")
            watch(map) {
                log("Receive $it")
                changes += it
            }
            map.entries.remove(map.entries.first().also { println("Entry: $it, HashCode: ${it.hashCode()}") })
            yield()
            yield()
        }
        assertEquals(MapChange.Remove(5, "5"), changes[1])
    }

    @Test fun replace() {
        runThenCancel {
            val map = watchableMapOf(5 to "5")
            watch(map) {
                changes += it
            }
            map[5] = "55"
            yield()
            yield()
        }

        assertEquals(MapChange.Initial(mapOf(5 to "5")), changes[0])
        assertEquals(MapChange.Replace(5, "5", "55"), changes[1])
        assertEquals(2, changes.size)
    }

    @Test fun replaceEntry() {
        runThenCancel {
            val map = watchableMapOf(5 to "5")
            watch(map) {
                log("Receive $it")
                changes += it
            }
            map.entries.first().setValue("55")
            yield()
            yield()
        }
        assertEquals(MapChange.Replace(5, "5", "55"), changes[1])
    }

    @Test fun readOnly() {
        runThenCancel {
            val map = watchableMapOf(5 to "5")
            val readOnly = map.readOnly().also {
                watch(it) { change -> changes += change }
            }
            Assert.assertThat(readOnly.toString(), CoreMatchers.startsWith("ReadOnlyWatchableMap("))
            map[5] = "55"
            assertEquals(1, readOnly.size)
            assertEquals(1, readOnly.entries.size)
            yield()
        }
        assertEquals(MapChange.Initial(mapOf(5 to "5")), changes[0])
        assertEquals(MapChange.Replace(5, "5", "55"), changes[1])
    }
}
