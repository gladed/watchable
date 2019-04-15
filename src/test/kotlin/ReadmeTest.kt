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

import io.gladed.watchable.WatchableList
import io.gladed.watchable.batch
import io.gladed.watchable.bind
import io.gladed.watchable.group
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.coroutineScope
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI

/** Check certain doc comments are accurate. */
class ReadmeTest {
    private val out = mutableListOf<String>()

    @Test fun `Watchable section`() = runTest {
        coroutineScope {
            val set = watchableSetOf(1, 2)
            watch(set) { println("Got $it") }
            set.add(3)
        }

        outputIs("""
            Got Initial(set=[1, 2])
            Got Add(add=[3])""")
    }

    @Suppress("UNUSED_VARIABLE")
    @Test fun `Watchable Data Types`() = runTest {
        val list = watchableListOf(1, 2, 3)
        val map = watchableMapOf(4 to "four")
        val set = watchableSetOf(5.0, 6.0)
        val value = watchableValueOf(URI.create("https://github.com"))
    }

    @Test fun `Reading and Writing Data`() = runTest {
        val map = watchableMapOf(1 to "1")
        println(map) // Prints {1=1}
        map.put(2, "2") // Suspends if concurrent modification attempted
        println(map) // Prints {1=1, 2=2}

        mutableListOf(1, 2, 3) += listOf(3, 4)
        outputIs("""
            {1=1}
            {1=1, 2=2}""")
    }

    @Test fun `Reading and Writing Data Part 2`() = runTest {
        val list = watchableListOf(1, 2, 3)
        // Remove last element safely
        println(list.use { removeAt(list.size - 1) }) // Prints 3
        outputIs("3")
    }

    @Test fun `Watching for Changes`() = runTest {
        val set = watchableSetOf(1, 2)
        watch(set) { println(it) }
        set += 3
        set -= listOf(3, 2)
        outputIs("""
            Initial(set=[1, 2])
            Add(add=[3])
            Remove(remove=[3, 2])""")
    }

    @Test fun `Binding - simple`() = runTest {
        val from = listOf(4, 5).toWatchableList()
        val into = watchableListOf<Int>()
        into.bind(this, from) // "this" is the current coroutine scope
        triggerActions() // not in doc
        // ...time passes...
        println(from == into) // true
        outputIs("""true""")
    }

    @Test fun `Binding - complex`() = runTest {
        val from = listOf(4, 5).toWatchableList()
        val into = watchableValueOf(0)
        into.bind(this, from) {
            // Ignore change (it) and read directly from source
            value = from.size
        }
        triggerActions() // not in doc
        // ...time passes...
        println(into) // true
        outputIs("""2""")
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

    private fun println(obj: Any) { out += obj.toString() }

    private fun TestCoroutineScope.outputIs(untrimmed: String) {
        triggerActions()
        assertEquals(untrimmed.trimIndent(), out.joinToString("\n"))
    }
}
