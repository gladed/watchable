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

import io.gladed.watchable.ListChange
import io.gladed.watchable.WatchableList
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WatchableListTest {

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)

    @Test fun add() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            val list = watchableListOf<Int>()
            watch(list) {
                log("Receive $it")
                changes += it
            }
            list.use { addAll(listOf(5, 6, 5)) }
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

    @Test fun equality() {
        runThenCancel {
            val list = watchableListOf(1, 2, 3)
            assertEquals(list.list, list.list)
            assertEquals(listOf(1, 2, 3), list.list)
            assertEquals(list.list, listOf(1, 2, 3))
            val list2 = watchableListOf(1, 2, 3)
            assertEquals(list.list, list2.list)
        }
    }

    @Test fun remove() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            val list = watchableListOf(5, 6)
            watch(list) {
                log("Receive $it")
                changes += it
            }
            list.use { remove(6) }
            yield()
            yield()
        }

        assertEquals(listOf(5, 6), (changes[0] as ListChange.Initial).initial)
        assertEquals(6, (changes[1] as ListChange.Remove).removed)
    }

    @Test fun update() {
        val changes = mutableListOf<ListChange<Int>>()
        runThenCancel {
            val list = watchableListOf(5, 6)
            watch(list) {
                log("Receive $it")
                changes += it
            }
            list.use { this[1] = 4 }
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
            val list = watchableListOf<Int>()
            val readOnly = list.readOnly().also {
                watch(it) { change ->
                    changes += change
                }
            }
            println("$readOnly") // Coverage
            list.use {
                addAll(listOf(5, 6))
                removeAll(listOf(6, 7, 8))
            }
            assertEquals(1, readOnly.list.size)
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
            val list = watchableListOf(3, 4)
            watch(list) { changes += it }
            yield()
            yield()
            list.use { clear() }
            yield()
            yield()
        }
        assertEquals(3, changes.size)
        assertEquals(3, (changes[1] as ListChange.Remove).removed)
        assertEquals(4, (changes[2] as ListChange.Remove).removed)
    }

    @Test fun dogPile() {
        fun pileOn(list: WatchableList<Int>, count: Int) = scope.launch {
            (0 until count).forEach { _ ->
                when ((Math.random() * 3).toInt()) {
                    0 -> list.use {
                            if (isNotEmpty()) {
                                val at = (size * Math.random()).toInt()
                                removeAt(at)
                            }
                        }
                    1 -> list.use {
                        val num = (Math.random() * 10).toInt()
                        add(num)
                    }
                    2 -> list.use {
                        if (isNotEmpty()) {
                            val at = (size * Math.random()).toInt()
                            val num = (Math.random() * 10).toInt()
                            this[at] = num
                        }
                    }
                }
            }
        }

        runThenCancel {
            val perPile = 100
            val list = watchableListOf(1)
            val list2 = watchableListOf(7)
            list2.bind(list)
            val jobs = listOf(pileOn(list, perPile), pileOn(list, perPile), pileOn(list, perPile), pileOn(list, perPile))
            (0 until 100).forEach { _ ->
                list.list.toString()
            }
            jobs.joinAll()
            delay(50)
            assertEquals(list2.list, list.list)
        }
    }
}
