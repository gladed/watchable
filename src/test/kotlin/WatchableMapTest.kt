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
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

class WatchableMapTest : ScopeTest() {
    @Rule @JvmField val changes = ChangeWatcherRule<MapChange<Int, String>>()

    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxKey = 500

    private val modifications = listOf<MutableMap<Int, String>.() -> Unit>(
        // Remove
        { chooser(keys)?.also { remove(it) } },

        // Replace (by map)
        { chooser(keys)?.also { key -> replace(key, this[key]!! + key) } },

        // Add/replace
        { chooser(maxKey).also { key -> this[key] = key.toString() } },

        // Add or replace (again, so we get a lot of insertions)
        { chooser(maxKey).also { key -> this[key] = key.toString() } },

        // Replace (by entry)
        { chooser(entries)?.apply { setValue("$value$key") } }
    )

    @Test fun changes() {
        val count = 1000
        val elapsed = measureTimeMillis {
            runToEnd {
                iterateMutable(
                    watchableMapOf(),
                    watchableMapOf(),
                    modifications,
                    { this[maxKey + 1] = "end" },
                    chooser,
                    count
                ).join()
            }
        }
        // With sync: 31 micros for 100k iters
        // Without sync: 26
        log("$count in $elapsed ms. ${elapsed * 1000 / count } μs per iteration.")
    }

    @Test fun entries() {
        runBlocking {
            val map = watchableMapOf(1 to "1")
            map.use {
                val first = entries.first()
                assertEquals(first, first)
                log("Entry: $entries has hash ${entries.hashCode()}") // Coverage
            }
        }
    }
    @Test fun replace() {
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

    @Test fun noEntryMod() {
        try {
            runToEnd {
                val map = watchableMapOf(1 to "1")
                map.use {
                    entries.add(entries.first())
                }
                fail("Shouldn't be able to add by entry")
            }
        } catch (e: UnsupportedOperationException) { }
    }
}
