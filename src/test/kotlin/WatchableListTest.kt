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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import javafx.collections.ObservableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.Collections
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
class WatchableListTest {

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    private val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)

    @Test fun add() {
        runThenCancel {
            val list = watchableListOf<Int>()
            watch(list) {
                log("Receive $it")
                launch {
                    changes.send(it)
                }
            }
            list.use { addAll(listOf(5, 6, 5)) }

            assertEquals(ListChange.Initial(listOf<Int>()), changes.receive())
            assertEquals(ListChange.Add(0, 5), changes.receive())
            assertEquals(ListChange.Add(1, 6), changes.receive())
            assertEquals(ListChange.Add(2, 5), changes.receive())
            assertTrue(changes.isEmpty)
        }
    }

    @Test fun equality() {
        runThenCancel {
            val list = watchableListOf(1, 2, 3)
            assertEquals(list, list)
            assertEquals(listOf(1, 2, 3), list)
            assertEquals(list, listOf(1, 2, 3))
            val list2 = watchableListOf(1, 2, 3)
            assertEquals(list, list2)
        }
    }

    @Test fun remove() {
        runThenCancel {
            val list = watchableListOf(5, 6)
            watch(list) {
                log("Receive $it")
                launch {
                    changes.send(it)
                }
            }
            list.use { remove(6) }
            assertEquals(ListChange.Initial(listOf(5, 6)), changes.receive())
            assertEquals(ListChange.Remove(1, 6), changes.receive())
        }
    }

    @Test fun update() {
        runThenCancel {
            val list = watchableListOf(5, 6)
            watch(list) {
                log("Receive $it")
                launch {
                    changes.send(it)
                }
            }
            list.use { this[1] = 4 }
            assertEquals(ListChange.Initial(listOf(5, 6)), changes.receive())
            assertEquals(ListChange.Replace(1, 6, 4), changes.receive())
        }
    }

    @Test fun readOnly() {
        runThenCancel {
            val list = watchableListOf<Int>()
            val readOnly = list.readOnly().also {
                watch(list) {
                    log("Receive $it")
                    launch {
                        changes.send(it)
                    }
                }
            }
            println("$readOnly") // Coverage
            list.use {
                addAll(listOf(5, 6))
                removeAll(listOf(6, 7, 8))
            }
            assertEquals(1, readOnly.size)

            assertEquals(ListChange.Initial(listOf<Int>()), changes.receive())
            assertEquals(ListChange.Add(0, 5), changes.receive())
            assertEquals(ListChange.Add(1, 6), changes.receive())
            assertEquals(ListChange.Remove(1, 6), changes.receive())
            delay(50)
            assertTrue(changes.isEmpty)
        }

    }

    @Test fun clear() {
        runThenCancel {
            val list = watchableListOf(3, 4)
            watch(list) {
                log("Receive $it")
                launch {
                    changes.send(it)
                }
            }
            list.use { clear() }

            assertEquals(ListChange.Initial(listOf(3, 4)), changes.receive())
            assertEquals(ListChange.Remove(0, 3), changes.receive())
            assertEquals(ListChange.Remove(0, 4), changes.receive())
            delay(50)
            assertTrue(changes.isEmpty)
        }
    }

    private fun MutableList<Int>.randomChange() {
        when ((Math.random() * 4).toInt()) {
            0 -> if (isNotEmpty()) {
                val at = (size * Math.random()).toInt()
                removeAt(at)
            }
            1 -> {
                add((Math.random() * 20).toInt())
            }
            2 -> if (isNotEmpty()) {
                val at = (size * Math.random()).toInt()
                val num = (Math.random() * 10).toInt()
                this[at] = num
            }
            3 -> { } // Sometimes just do nothing
        }
    }

    private fun pileOn(count: Int, block: () -> Unit) = scope.launch {
        (0 until count).forEach { _ ->
            block()
        }
    }

    @Test fun syncPerfCheck() {
        val list = Collections.synchronizedList(mutableListOf(1, 2, 3, 4, 5, 6))
        val elapsed = measureTimeMillis {
            runThenCancel {
                val perPile = PER_PILE
                val act = {
                    synchronized(list) {
                        list.randomChange()
                        list.randomChange()
                    }
                }

                val jobs = listOf(pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act))
                jobs.joinAll()
            }
        }
        // About 55ms
        println("syncPerfCheck: $elapsed")
    }

    @Test fun perfCheck() {
        val elapsed = measureTimeMillis {
            runThenCancel {
                val perPile = PER_PILE
                val list = watchableListOf(1, 2, 3, 4, 5, 6)
                // List is watchable but nobody is actually watching.
                val act = {
                    list.use {
                        randomChange()
                        randomChange()
                    }
                }

                val jobs = listOf(pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act))
                jobs.joinAll()
            }
        }
        // About 340ms
        println("perfCheck: $elapsed")
    }

    @Test fun perfBindCheck() {
        val start = System.currentTimeMillis()
        var listMatch = false
        val elapsed = measureTimeMillis {
            runThenCancel {
                val perPile = PER_PILE
                val list = watchableListOf(1, 2, 3, 4, 5, 6)
                val list2 = watchableListOf(7, 8, 9)
                list2.bind(list)
                val act = {
                    list.use {
                        randomChange()
                        randomChange()
                    }
                }

                val jobs = listOf(pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act))
                jobs.joinAll()

                log("Done, waiting for alignment after ${System.currentTimeMillis() - start}")
                watch(list2) {
                    if (list2 == list) {
                        listMatch = true
                        coroutineContext.cancel()
                    }
                }
                delay(4000) // Wait for lists to align (they had better)
            }
        }
        assertTrue(listMatch)
        // About 774ms
        println("perfBindCheck: $elapsed")
    }

    companion object {
        const val PER_PILE = 10000 // Increase to run larger-scale performance tests
    }
}
