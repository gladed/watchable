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

import com.gladed.watchable.ListChange
import com.gladed.watchable.WatchableList
import com.gladed.watchable.watchableListOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchableListTest {
    private lateinit var list: WatchableList<Int>

    // A way to run a blocking section then cancel the enclosing scope
    private fun <T> runThenCancel(block: suspend CoroutineScope.() -> T) {
        try {
            runBlocking {
                block().also {
                    coroutineContext.cancel()
                }
            }
        } catch (e: CancellationException) {
            // As expected
        }
    }

    @Test fun add() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            list = watchableListOf()
            list.watch {
                log("Receive $it")
                changes += it
            }
            list.addAll(listOf(5, 6, 5))
            yield()
            yield()
        }

        assertTrue(changes[0] is ListChange.Initial)
        assertEquals(5, (changes[1] as ListChange.Add).added)
        assertEquals(6, (changes[2] as ListChange.Add).added)
        assertEquals(5, (changes[3] as ListChange.Add).added)
        assertEquals(2, (changes[3] as ListChange.Add).index)
        assertEquals(4, changes.size)
    }

    @Test fun remove() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            list = watchableListOf(5, 6)
            list.watch {
                log("Receive $it")
                changes += it
            }
            list.remove(6)
            yield()
            yield()
        }

        assertEquals(listOf(5, 6), (changes[0] as ListChange.Initial).initial)
        assertEquals(6, (changes[1] as ListChange.Remove).removed)
    }

    @Test fun update() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            list = watchableListOf(5, 6)
            list.watch {
                log("Receive $it")
                changes += it
            }
            list[1] = 4
            yield()
            yield()
        }

        assertEquals(listOf(5, 6), (changes[0] as ListChange.Initial).initial)
        assertEquals(6, (changes[1] as ListChange.Replace).removed)
        assertEquals(4, (changes[1] as ListChange.Replace).added)
    }

    @Test fun readOnly() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            list = watchableListOf()
            val readOnly = list.readOnly().also {
                it.watch { change ->
                    changes += change
                }
            }
            println("$readOnly") // Coverage
            list.addAll(listOf(5, 6))
            list.removeAll(listOf(6, 7, 8))
            assertEquals(1, readOnly.size)
            yield()
        }

        assertTrue(changes[0] is ListChange.Initial)
        assertEquals(5, (changes[1] as ListChange.Add).added)
        assertEquals(6, (changes[2] as ListChange.Add).added)
        assertEquals(6, (changes[3] as ListChange.Remove).removed)
    }

    @Test fun clear() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            list = watchableListOf(3, 4)
            list.watch { changes += it }
            yield()
            yield()
            list.clear()
            yield()
            yield()
        }
        assertEquals(3, changes.size)
        assertEquals(3, (changes[1] as ListChange.Remove).removed)
        assertEquals(4, (changes[2] as ListChange.Remove).removed)
    }

    // TODO: Bind with replace etc.
}
