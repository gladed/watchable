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
import io.gladed.watchable.bind
import io.gladed.watchable.group
import io.gladed.watchable.simple
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI

/** Check certain doc comments are accurate. */
class ReadmeTest {
    private val out = mutableListOf<String>()

    @Test fun `Watchable section`() = runTest {
        coroutineScope {
            val set = watchableSetOf(1, 2)
            watch(set) { println("Got $it") }.start()
            set.add(3)

            outputIs("""
                Got Initial(set=[1, 2])
                Got Add(add=[3])""")
        }
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
        println(list { removeAt(list.size - 1) }) // Prints 3
        outputIs("3")
    }

    @Test fun `Watching for Changes`() = runTest {
        val set = watchableSetOf(1, 2)
        watch(set) { println(it) }.start()
        set += 3
        set -= listOf(3, 2)
        outputIs("""
            Initial(set=[1, 2])
            Add(add=[3])
            Remove(remove=[3, 2])""")
    }

    @Test fun `Binding - simple`() = runTest {
        val origin = listOf(4, 5).toWatchableList()
        val target = watchableListOf<Int>()
        bind(target, origin).start()
        println(origin == target) // true

        outputIs("""true""")
    }

    @Test fun `Binding - complex`() = runTest {
        val origin = listOf(4, 5).toWatchableList()
        val target = watchableValueOf(0)
        bind(target, origin) {
            // Update value with current origin size
            value = origin.size
        }.start()
        println(target) // 2

        outputIs("""2""")
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @Test fun `Batching readme`() = runTest {
        pauseDispatcher {
            val list = listOf(4, 5).toWatchableList()
            batch(list, 50) { println(it) }.start()
            list { add(6); add(7) }
            // After time passes, prints:
            // [Initial(list=[4, 5]), Add(index=2, added=6), Add(index=3, added=7)]
        }
        outputIs("""
            [Initial(list=[4, 5]), Insert(index=2, insert=[6, 7])]""")
    }

    @Test fun `Grouping readme`() = runTest {
        val set = setOf("a").toWatchableSet()
        val list = listOf(4).toWatchableList()
        watch(group(set, list)) { println(it) }
        outputIs("""
            GroupChange(watchable=[a], change=Initial(set=[a]))
            GroupChange(watchable=[4], change=Initial(list=[4]))""")

        out.clear()
        list += 6
        set += "b"
        outputIs("""
            GroupChange(watchable=[4, 6], change=Insert(index=1, insert=[6]))
            GroupChange(watchable=[a, b], change=Add(add=[b]))""")
    }

    @Test fun `Simple Watches`() = runTest {
        val map = watchableMapOf(1 to "2")
        simple(map) { with(it) { println("at $key remove $remove add $add") } }.start()
        map.put(1, "3")

        outputIs("""
            at 1 remove null add 2
            at 1 remove 2 add 3""".trimIndent())
    }

    @Test fun `Object Lifetime`() = runTest {
        val list = watchableListOf(1)
        val handle = watch(list) { println(it) }
        handle.start()
        list.add(2)
        handle.stop() // deliver prior changes, but don't watch for new changes
        list.add(3)

        outputIs("""
            Initial(list=[1])
            Insert(index=1, insert=[2])""".trimIndent())
    }

    @Test fun `Object Lifetime Part 2`() = runTest {
        val list = watchableListOf(1)
        launch {
            // Note: you must refer to the outer scope.
            this@runTest.watch(list) { println(it) }
        }
        list.add(2)
        list.add(3)
        outputIs("""
        Initial(list=[1])
        Insert(index=1, insert=[2])
        Insert(index=2, insert=[3])""".trimIndent())
    }

    private fun println(obj: Any) { out += obj.toString() }

    private fun outputIs(untrimmed: String) {
        assertEquals(untrimmed.trimIndent(), out.joinToString("\n"))
    }
}
