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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.none
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
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
                changes.send(it)
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
            assertEquals(list.list, list.list)
            assertEquals(listOf(1, 2, 3), list.list)
            assertEquals(list.list, listOf(1, 2, 3))
            val list2 = watchableListOf(1, 2, 3)
            assertEquals(list.list, list2.list)
        }
    }

    @Test fun remove() {
        runThenCancel {
            val list = watchableListOf(5, 6)
            watch(list) {
                log("Receive $it")
                changes.send(it)
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
                changes.send(it)
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
                    changes.send(it)
                }
            }
            println("$readOnly") // Coverage
            list.use {
                addAll(listOf(5, 6))
                removeAll(listOf(6, 7, 8))
            }
            assertEquals(1, readOnly.list.size)

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
                changes.send(it)
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
        when ((Math.random() * 3).toInt()) {
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
        }
    }

    private fun pileOn(count: Int, block: suspend () -> Unit) = scope.launch {
        (0 until count).forEach { _ ->
            block()
        }
    }

    @Test fun dogPile() {
        runThenCancel {
            val perPile = 100
            val list = watchableListOf(1)
            val list2 = watchableListOf(7)
            log("Binding $list to $list2")
            list2.bind(list)
            val act = suspend {
                list.use { randomChange(); randomChange() }
            }
            val jobs = listOf(pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act))
            (0 until 200).forEach { _ ->
                // Prove that ConcurrentModificationException will not happen
                list.list.toString()
            }
            jobs.joinAll()
            delay(50)
            assertEquals(list2.list, list.list)
        }
    }

    @Test fun perfCheck() {
        val start = System.currentTimeMillis()
        val elapsed = measureTimeMillis {
            runThenCancel {
                val perPile = 10000
                val list = watchableListOf(1, 2, 3, 4, 5, 6)
                val list2 = watchableListOf(7, 8, 9)
                list2.bind(list)
                val act = suspend {
                    list.use { randomChange(); randomChange() }
                }

                val jobs = listOf(pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act), pileOn(perPile, act))
                jobs.joinAll()

                log("Done, waiting for alignment after ${System.currentTimeMillis() - start}")
                watch(list2) {
                    if (list2.list == list.list) {
                        log("Alignment with ${list.list}")
                        coroutineContext.cancel()
                    }
                }
                delay(8000) // Wait up to 8 seconds for the correct list to finally filter into list2.
            }
        }
        // At present about 2100ms-2300ms
        println("Elapsed: $elapsed")
    }

}
