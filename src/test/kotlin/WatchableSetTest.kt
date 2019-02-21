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

import com.gladed.watchable.SetChange
import com.gladed.watchable.WatchableSet
import com.gladed.watchable.watchableSetOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchableSetTest {
    private lateinit var set: WatchableSet<Int>

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
        val changes = mutableListOf<SetChange<Int>>()
        runThenCancel {
            set = watchableSetOf()
            set.watch {
                log("Receive $it")
                changes += it
            }
            set.addAll(listOf(5, 6, 5))
            yield()
            yield()
        }

        assertTrue(changes[0] is SetChange.Initial)
        assertEquals(5, (changes[1] as SetChange.Add).added)
        assertEquals(6, (changes[2] as SetChange.Add).added)
        assertEquals(3, changes.size)
    }

    @Test fun remove() {
        val changes = mutableListOf<SetChange<Int>>()
        runThenCancel {
            set = watchableSetOf(5, 6)
            set.watch {
                log("Receive $it")
                changes += it
            }
            set.remove(6)
            yield()
            yield()
        }

        assertEquals(setOf(5, 6), (changes[0] as SetChange.Initial).initial)
        assertEquals(6, (changes[1] as SetChange.Remove).removed)
    }

    @Test fun readOnly() {
        val changes = mutableListOf<SetChange<Int>>()
        runThenCancel {
            set = watchableSetOf()
            val readOnly = set.readOnly().also {
                it.watch { change ->
                    changes += change
                }
            }
            println("$readOnly") // Coverage
            set.addAll(listOf(5, 6))
            set.removeAll(listOf(6, 7, 8))
            assertEquals(1, readOnly.size)
            yield()
        }

        assertTrue(changes[0] is SetChange.Initial)
        assertEquals(5, (changes[1] as SetChange.Add).added)
        assertEquals(6, (changes[2] as SetChange.Add).added)
        assertEquals(6, (changes[3] as SetChange.Remove).removed)
    }
}
