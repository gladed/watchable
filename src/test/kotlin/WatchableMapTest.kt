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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.yield
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class WatchableMapTest {
    private val chooser = Chooser(0) // Seed of 0 makes this test repeatable
    private val modifications = listOf<MutableMap<Int, String>.(Chooser) -> Unit>(
        // Remove
        { chooser -> chooser(keys)?.also { key: Int -> remove(key) } },
        // Replace (by map)
        { chooser -> chooser(keys)?.also { key: Int ->
            replace(key, this[key]!! + key.toString()) } },
        // Add or replace (maybe new)
        { chooser -> val n = chooser(200); this[n] = n.toString() },
        // Add or replace (maybe new)
        { chooser -> val n = chooser(200); this[n] = n.toString() },
        // Replace (by entry)
        { chooser ->
            chooser(entries)?.also { entry ->
                entry.setValue(entry.value + entry.key.toString())
            }
        }
    )
    private fun randomModification() = chooser.invoke(modifications)!!

    @Test fun changeTest() {
        runThenCancel {
            val map = watchableMapOf<Int, String>()
            assertThat(map.toString(), startsWith("WatchableMap("))

            // Create a second map which is bound to the first map
            val map2 = watchableMapOf<Int, String>()
            map2.bind(map)

            // Create a third map which is a read-only shell around the bound map
            val map3 = map2.readOnly()
            assertThat(map3.toString(), startsWith("ReadOnlyWatchableMap("))

            // Make a bunch of random modifications
            (0 until 250).forEach { _ ->
                map.use {
                    randomModification()(this, chooser)
                }
                yield()
            }

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

            // Watch the read-only map until it catches up the original map and cancel.
            watch(map3) {
                // Assert that map2 reaches equality with map1
                if (map3 == map && coroutineContext.isActive) {
                    coroutineContext.cancel()
                }
            }

            delay(2000) // Give the above time to wrap up
            assertEquals(map, map3)
            assertTrue(map3 == map)
        }
    }
}
