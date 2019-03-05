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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class BindSetTest {
    @Rule @JvmField val changes = ChangeWatcherRule<SetChange<Int>>()

    @Test fun bind() {
        runBlocking {
            val origin = watchableSetOf(5)
            val dest = watchableSetOf(6)
            dest.bind(origin)
            eventually { assertEquals(setOf(5), dest.get()) }
        }
    }

    @Test fun bindThenChange() {
        runBlocking {
            val origin = watchableSetOf(4)
            val dest = watchableSetOf<Int>()
            dest.bind(origin)
            origin.use {
                add(6)
                add(5)
                remove(4)
            }
            eventually { assertEquals(setOf(5, 6), dest.get()) }
            // Order matters to iterators
            assertEquals(6, dest.get().iterator().next())
        }
    }

    @Test fun badAdd() {
        try {
            runBlocking {
                val origin = watchableSetOf(4, 5)
                val dest = watchableSetOf(6)
                dest.bind(origin)
                dest.use {
                    add(7)
                }
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun badRemove() {
        try {
            runBlocking {
                val origin = watchableSetOf(4, 5)
                val dest = watchableSetOf(5)
                dest.bind(origin)
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
        runBlocking {
            val origin = watchableSetOf(4, 5)
            val dest = watchableSetOf(6)
            dest.bind(origin)
            origin.use {
                addAll(listOf(8, 7))
            }
            eventually { assertEquals(setOf(4, 5, 8, 7), dest.get() ) }
            dest.unbind()
            origin.use {
                remove(5)
            }
            always { assertTrue(dest.get().contains(5)) }
        }
    }
}
