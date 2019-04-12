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

import io.gladed.watchable.batch
import io.gladed.watchable.group
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import org.junit.Assert.assertEquals
import org.junit.Test

/** Check certain doc comments are accurate. */
class ReadmeTest {
    val out = mutableListOf<String>()

    @Test fun `batching docs`() = runTest {
        val list = listOf(4, 5).toWatchableList()
        batch(list) { out += it.toString() }
        list.use {
            add(6)
            add(7)
            remove(6)
        }
        triggerActions()
        assertEquals("""
            [Initial(list=[4, 5])]
            [Insert(index=2, insert=[6]), Insert(index=3, insert=[7]), Remove(index=2, remove=6)]""".trimIndent(),
            out.joinToString("\n"))

        // Prints: [Add(index=2, added=6), Add(index=3, added=7)]
    }

    @Test fun `close watch handle`() = runTest {
        val list = watchableListOf(1)
        val handle = watch(list) { out += it.toString() }
        list.add(2)
        handle.close()
        list.add(3)
        triggerActions()
        assertEquals("""
            Initial(list=[1])
            Insert(index=1, insert=[2])""".trimIndent(), out.joinToString("\n"))
    }

    @Test fun `example from readme`() = runTest {
        val list = listOf(4).toWatchableList()
        val set = setOf("a").toWatchableSet()
        watch(group(set, list)) { println(it) }
        triggerActions()
        // Prints:
        //   GroupChange(watchable=WatchableSet(), change=Initial(initial=[a]))
        //   GroupChange(watchable=WatchableList(), change=Initial(initial=[4]))
        list.use { add(6) }
        set.use { add("b") }
        //   GroupChange(watchable=WatchableList(), change=Add(index=1, added=6))
        //   GroupChange(watchable=WatchableSet(), change=Add(added=b))
        triggerActions()
    }
}
