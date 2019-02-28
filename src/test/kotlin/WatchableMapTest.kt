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

import io.gladed.watchable.watch
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class WatchableMapTest : CoroutineScope {

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext

    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxKey = 500

    private val modifications = listOf<MutableMap<Int, String>.(Chooser) -> String>(
        // Remove
        { chooser -> chooser(keys)?.let { key: Int ->
            val removed = remove(key)
            "Remove(key=$key, removed=$removed)"
        } ?: "none" },

        // Replace (by map)
        { chooser -> chooser(keys)?.let { key: Int ->
            val current = this[key]!!
            val value = current + key.toString()
            replace(key, value)
            "Replace(key=$key, removed=$current, added=$value)"
        } ?: "none" },

        // Add or replace
        { chooser ->
            val n = chooser(maxKey)
            val removed = this[n]
            this[n] = n.toString()
            if (removed == null) "Add(key=$n, added=$n)" else "Replace(key=$n, removed=$removed, added=$n)"
        },

        // Add or replace (again, so we get a lot of insertions)
        { chooser ->
            val n = chooser(maxKey)
            val removed = this[n]
            this[n] = n.toString()
            if (removed == null) "Add(key=$n, added=$n)" else "Replace(key=$n, removed=$removed, added=$n)"
        },

        // Replace (by entry)
        { chooser ->
            chooser(entries)?.let { entry ->
                val removed = entry.value
                val value = entry.value + entry.key.toString()
                entry.setValue(entry.value + entry.key.toString())
                "entry.Replace(key=${entry.key}, removed=$removed, added=$value)"
            } ?: "none"
        }
    )

    @Test fun changes() {
        runToEnd {
            val map = watchableMapOf(1 to "1")
            assertThat(map.toString(), startsWith("WatchableMap("))

            // Create a second map which is bound to the first map
            val map2 = watchableMapOf<Int, String>()
            map2.bind(map)

            // Create a third map which is a read-only shell around the bound map
            val map3 = map2.readOnly()
            assertThat(map3.toString(), startsWith("ReadOnlyWatchableMap("))

            // Confirm a few things about the map
            map.use {
                println("Map first entry is ${entries.first()} and its hashcode is ${entries.first().hashCode()}")
                // Show that we can't add entries this way, only through "put"
                try {
                    entries.add(entries.first())
                } catch (e: UnsupportedOperationException) {
                    // Expected
                }
            }

            // Make a bunch of random modifications
            (0 until 5000).map {
                launch {
                    map.use {
                        chooser.invoke(modifications)!!(this, chooser)
                    }
                }
            }.joinAll()

            // Write a special key at the end
            map.use {
                this[maxKey + 1] = "end"
            }

            // Watch the read-only map until it catches up the original map and cancel.
            watch(map3) {
                // Wait for map3 to reach equality with map
                if (map3 == map) {
                    log("map=$map")
                    log("map2=$map3")
                    coroutineContext.cancel()
                }
            }
            // Give the above time to wrap up, if it doesn't, assert on the reason:
            delay(2000)
            assertEquals(map, map3)
            assertTrue(map3 == map)
        }
    }
}
