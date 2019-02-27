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

import io.gladed.watchable.SetChange
import io.gladed.watchable.WatchableSet
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.yield
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class WatchableSetTest {
    private lateinit var set: WatchableSet<Int>
    private val changes = mutableListOf<SetChange<Int>>()

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)

    @Test fun add() {
        val latch = CountDownLatch(3) // Expect 3 events
        set = scope.watchableSetOf()
        scope.watch(set) {
            log("Receive $it")
            changes += it
            latch.countDown()
        }
        set.use { addAll(listOf(6, 5)) }

        latch.await()
        assertThat(set.toString(), startsWith("WatchableSet("))
        assertTrue(changes[0] is SetChange.Initial)
        assertEquals(6, (changes[1] as SetChange.Add).added)
        assertEquals(5, (changes[2] as SetChange.Add).added)
        assertEquals(3, changes.size)
    }

    @Test fun remove() {
        runThenCancel {
            set = watchableSetOf(5, 6)
            watch(set) {
                log("Receive $it")
                changes += it
            }
            set.use { remove(6)}
            yield()
            yield()
        }

        assertEquals(setOf(5, 6), (changes[0] as SetChange.Initial).initial)
        assertEquals(6, (changes[1] as SetChange.Remove).removed)
    }

    @Test fun readOnly() {
        runThenCancel {
            set = watchableSetOf()
            val readOnly = set.readOnly().also {
                watch(it) { change ->
                    changes += change
                }
            }
            println("$readOnly") // Coverage
            set.use {
                addAll(listOf(5, 6))
                removeAll(listOf(6, 7, 8))
            }
            assertEquals(1, readOnly.size)
            yield()
        }

        assertTrue(changes[0] is SetChange.Initial)
        assertEquals(5, (changes[1] as SetChange.Add).added)
        assertEquals(6, (changes[2] as SetChange.Add).added)
        assertEquals(6, (changes[3] as SetChange.Remove).removed)
    }
}
