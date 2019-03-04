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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableMapOf
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class BindMapTest {
    @Rule @JvmField val changes = ChangeWatcherRule<MapChange<Int, String>>()

    @Test fun bind() {
        runThenCancel {
            val origin = watchableMapOf(5 to "5")
            val dest = watchableMapOf(6 to "6")
            dest.bind(origin)
            watch(dest) { changes += it }
            changes.expect(MapChange.Initial(mapOf(5 to "5")))
            assertEquals(mapOf(5 to "5"), dest.get())
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableMapOf(5 to "5")
            val dest = watchableMapOf(6 to "6")
            watch(dest) { changes += it }
            dest.bind(origin)
            changes.expect(MapChange.Initial(mapOf(6 to "6")))
            changes.expect(MapChange.Remove(6, "6"))
            changes.expect(MapChange.Add(5, "5"))
            origin.use {
                this[7] = "7"
                this -= 5
                this[7] = "77"
            }
            origin.use {
                put(9, "9")
            }
            changes.expect(MapChange.Add(7, "7"))
            changes.expect(MapChange.Remove(5, "5"))
            changes.expect(MapChange.Replace(7, "7", "77"))
            changes.expect(MapChange.Add(9, "9"))

            assertEquals(origin.get(), dest.get())
        }
    }

    @Test fun badAdd() {
        try {
            runThenCancel {
                val origin = watchableMapOf(5 to "5")
                val dest = watchableMapOf(6 to "6")
                dest.bind(origin)
                dest.use { put(7, "7") }
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun badRemove() {
        try {
            runThenCancel {
                val origin = watchableMapOf(5 to "5")
                val dest = watchableMapOf(6 to "6")
                dest.bind(origin)
                dest.use {
                    remove(6)
                    remove(5)
                }
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbindThenChange() {
        runThenCancel {
            val origin = watchableMapOf(5 to "5")
            val dest = watchableMapOf(6 to "6")
            dest.bind(origin)
            watch(dest) { changes += it }
            changes.expect(MapChange.Initial(mapOf(5 to "5")))
            origin.use {
                putAll(listOf(8 to "8", 7 to "7"))
            }
            changes.expect(MapChange.Add(8, "8"), MapChange.Add(7, "7"))
            dest.unbind()
            origin.use {
                remove(5)
            }
            assertEquals(mapOf(5 to "5", 8 to "8", 7 to "7"), dest.get())
        }
    }
}
