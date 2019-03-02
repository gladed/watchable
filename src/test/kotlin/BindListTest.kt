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
import io.gladed.watchable.ValueChange
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class BindListTest {
    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test fun bind() {
        runThenCancel {
            val origin = watchableListOf(5)
            val dest = watchableListOf(6)
            bind(origin, dest)
            watch(dest) { changes += it }
            changes.expect(ListChange.Initial(listOf(5)))
            assertEquals(listOf(5), dest.get())
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableListOf(4, 5)
            val dest = watchableListOf(6)
            assertFalse(dest.isBound())
            bind(origin, dest)
            assertTrue(dest.isBound())
            watch(dest) { changes += it }
            changes.expect(ListChange.Initial(listOf(4, 5)))

            origin.use {
                addAll(listOf(8, 7))
                remove(5)
            }
            origin.use {
                add(9)
                remove(4)
                this[1] = 11
            }
            changes.expect(
                ListChange.Add(2, 8),
                ListChange.Add(3, 7),
                ListChange.Remove(1, 5),
                ListChange.Add(3, 9),
                ListChange.Remove(0, 4),
                ListChange.Replace(1, 7, 11))

            assertEquals(listOf(8, 11, 9), dest.get())
        }
    }

    @Test fun badWrite() {
        try {
            runThenCancel {
                val origin = watchableListOf(4, 5)
                val dest = watchableListOf(6)
                bind(origin, dest)
                dest.use { add(5) }
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbindThenChange() {
        runThenCancel {
            val origin = watchableListOf(4, 5)
            val dest = watchableListOf(6)
            bind(origin, dest)
            watch(dest) { changes += it }
            changes.expect(ListChange.Initial(listOf(4, 5)))
            origin.use { addAll(listOf(8, 7)) }
            changes.expect(ListChange.Add(2, 8), ListChange.Add(3, 7))
            dest.unbind()
            dest.unbind() // twice to show it works ok
            origin.use { remove(5) }
            changes.expectNone()
            assertEquals(listOf(4, 5, 8, 7), dest.get())
        }
    }
}
