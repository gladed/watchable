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
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WatchableListTest : CoroutineScope {
    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext

    private val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxValue = 100

    private val mods = listOf<MutableList<Int>.() -> Unit>(
        { if (isNotEmpty()) removeAt(chooser(size)) },
        { add(chooser(maxValue)) },
        { if (isNotEmpty()) chooser(size).also { at -> this[at] = chooser(maxValue) } }
    )

    @Test fun changes() {
        runToEnd {
            val list = watchableListOf(1, 2, 3)
            val list2 = watchableListOf<Int>()
            list2.bind(list)
            val list3 = list2.readOnly()

            assertThat(list.toString(), startsWith("WatchableList("))
            assertThat(list3.toString(), startsWith("ReadOnlyWatchableList("))

            (0 until 10000).map {
                launch {
                    when (chooser(3)) {
                        // Show that it's safe to access the list
                        0 -> list3.get().apply { if (isNotEmpty()) last() }
                        else -> list.use { chooser(mods)!!(this) }
                    }
                }
            }.joinAll()

            // Add something special
            list.use {
                add(maxValue + 1)
            }

            list3.watch {
                if (list3.get() == list.get()) {
                    coroutineContext.cancel()
                }
            }
            delay(2000)
            assertEquals(list.get(), list3.get())
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
            list.watch {
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
}
