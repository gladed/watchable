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
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class BindListTest {
    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test fun bind() {
        runBlocking {
            val origin = watchableListOf(5)
            val dest = watchableListOf<Int>()
            dest.bind(origin)
            eventually { assertEquals(listOf(5), dest.get()) }
        }
    }

    @Test fun bindThenChange() {
        runBlocking {
            val origin = watchableListOf(4, 5)
            val dest = watchableListOf<Int>()
            assertFalse(dest.isBound())
            dest.bind(origin)
            assertTrue(dest.isBound())
            watch(dest) { changes += it }

            origin.use {
                addAll(listOf(8, 7))
                remove(5)
            }

            origin.use {
                add(9)
                remove(4)
                this[1] = 11
            }

            eventually { assertEquals(listOf(8, 11, 9), dest.get()) }
        }
    }

    @Test fun badWrite() {
        try {
            runBlocking {
                val origin = watchableListOf(4, 5)
                val dest = watchableListOf(6)
                dest.bind(origin)
                dest.use { add(5) }
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbindThenChange() {
        runBlocking {
            val origin = watchableListOf(4)
            val dest = watchableListOf<Int>()
            dest.bind(origin)
            origin.use { addAll(listOf(8, 7)) }
            eventually { assertEquals(listOf(4, 8, 7), dest.get()) }
            dest.unbind()
            dest.unbind() // twice to show it works ok
            origin.use { remove(4) }
            always { assertEquals(listOf(4, 8, 7), dest.get()) }
        }
    }

    @Test fun specialBinding() {
        runBlocking {
            // Show how we can transform data types into each other
            val origin = watchableListOf(4, 5)
            val dest = watchableMapOf(0 to 0)

            fun MutableMap<Int, Int>.increment(key: Int) {
                val addCount = this[key]
                when(addCount) {
                    null -> this[key] = 1
                    else -> this[key] = addCount + 1
                }
            }

            fun MutableMap<Int, Int>.decrement(key: Int) {
                val removeCount = this[key]
                when(removeCount) {
                    null -> remove(key) // Shouldn't happen really
                    1 -> remove(key)
                    else -> this[key] = removeCount - 1
                }
            }

            dest.bind(origin) {
                println("Handling $it")
                when(it) {
                    is ListChange.Initial -> {
                        clear()
                        for (key in it.initial) {
                            increment(key)
                        }
                    }
                    is ListChange.Add -> increment(it.added)
                    is ListChange.Remove -> decrement(it.removed)
                    is ListChange.Replace -> {
                        increment(it.added)
                        decrement(it.removed)
                    }
                }
            }

            origin.use {
                add(4)
                set(1, 6)
            }

            eventually {
                dest.get() == mapOf(4 to 2, 6 to 1)
            }
        }
    }
}
