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

import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.lang.IllegalStateException

class BindMapTest {
    @Test fun bind() {
        runThenCancel {
            val origin = watchableMapOf(5 to "5")
            val dest = watchableMapOf(6 to "6")
            dest.bind(origin)
            delay(50)
            assertEquals(mapOf(5 to "5"), dest.map)
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableMapOf(5 to "5")
            val dest = watchableMapOf(6 to "6")
            dest.bind(origin)
            origin.use {
                this[7] = "7"
                this -= 5
                this[7] = "77"
            }
            delay(50)
            origin.use {
                put(9, "9")
            }
            delay(50)
            assertEquals(mapOf(7 to "77", 9 to "9"), dest.map)
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
                delay(50)
                dest.use {
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
            origin.use {
                putAll(listOf(8 to "8", 7 to "7"))
            }
            delay(50)
            dest.unbind()
            origin.use {
                remove(5)
            }
            delay(50)
            assertEquals(mapOf(5 to "5", 8 to "8", 7 to "7"), dest.map)
        }
    }
}
