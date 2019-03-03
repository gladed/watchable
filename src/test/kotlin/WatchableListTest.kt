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
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import io.gladed.watchable.watchBatches
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WatchableListTest : CoroutineScope {
    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext

    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxValue = 100

    private val mods = listOf<MutableList<Int>.() -> Unit>(
        { if (isNotEmpty()) removeAt(chooser(size)) },
        { add(chooser(maxValue)) },
        { if (isNotEmpty()) chooser(size).also { at -> this[at] = chooser(maxValue) } }
    )

    @Test fun changes() {
        runToEnd {
            iterateMutable(this, watchableListOf(1, 2, 3), watchableListOf(4), mods, { add(maxValue + 1) },
                chooser)
        }
    }


    @Test fun equality() {
        runThenCancel {
            val list = watchableListOf(1, 2, 3)
            assertEquals(list.get(), list.get())
            assertEquals(listOf(1, 2, 3), list.get())
            assertEquals(list.get(), listOf(1, 2, 3))
            val list2 = watchableListOf(1, 2, 3)
            assertEquals(list.get(), list2.get())
        }
    }

    @Test fun clear() {
        runThenCancel {
            val list = watchableListOf(3, 4)
            watchBatches(list) { changes += it }
            changes.expect(ListChange.Initial(listOf(3, 4)))
            list.use { clear() }
            changes.expect(ListChange.Remove(0, 3), ListChange.Remove(0, 4))
            changes.expectNone()
        }
    }

    @Test fun replace() {
        runToEnd {
            val list = watchableListOf(1)
            val list2 = watchableListOf(2)
            bind(list, list2)
            val list3 = list2.readOnly()
            watch(list3) { changes += it}
            assertThat(list.toString(), startsWith("WatchableList("))
            assertThat(list3.toString(), startsWith("ReadOnlyWatchableList("))
            changes.expect(ListChange.Initial(listOf(1)))
            list.set(listOf(3))
            changes.expect(ListChange.Remove(0, 1), ListChange.Add(0, 3))
        }
    }
}
