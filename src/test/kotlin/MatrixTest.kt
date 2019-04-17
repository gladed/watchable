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

import io.gladed.watchable.Change
import io.gladed.watchable.MutableWatchable
import io.gladed.watchable.bind
import io.gladed.watchable.waitFor
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MatrixTest<M, C: Change> {
    private val chooser = Chooser()

    @Suppress("unused")
    @Parameter(0) lateinit var name: String
    @Parameter(1) lateinit var maker1: () -> MutableWatchable<M, C>
    @Parameter(2) lateinit var maker2: () -> MutableWatchable<M, C>
    @Parameter(3) lateinit var modificationMaker: (M, Chooser) -> ((M) -> Unit)
    @Parameter(4) lateinit var finalMod: (M) -> Unit


    private lateinit var watchable1: MutableWatchable<M, C>
    private lateinit var watchable2: MutableWatchable<M, C>

    /** Apply a random modification to this [M]. */
    private fun M.modify() {
        modificationMaker(this, chooser).invoke(this)
    }


    @Before fun setup() {
        watchable1 = maker1()
        watchable2 = maker2()
    }

    @Test(timeout = 250) fun `bind works`() = runTest {
        assertFalse(watchable2.isBound())
        bind(watchable2, watchable1)
        assertTrue(watchable2.isBound())
        waitFor(watchable2) { it == watchable1 }
    }

    @Test fun `bind then make changes`() = runTest {
        bind(watchable2, watchable1)
        for (i in 0 until 200) {
            watchable1.use { modify() }
        }
        assertNotEquals(watchable1, watchable2)
        waitFor(watchable2) { it == watchable1 }
        log(watchable1)
    }

    @Test fun `no change on bound`() = runTest {
        bind(watchable2, watchable1).start()
        try {
            for (i in 0 until 100) watchable2.use { modify() }
            fail("Should not have worked")
        } catch (e: IllegalStateException) {
            log("Correct failure: $e")
        }
    }

    @Test fun `no self-bind`() = runTest {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable1, watchable1)
        }
    }

    @Test fun `no circular-bind`() = runTest {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable2, watchable1)
            bind(watchable1, watchable2)
        }
    }

    @Test fun `no write when bound`() = runTest {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable2, watchable1)
            for (i in 0 until 20) {
                watchable2.use { modify() }
            }
        }
    }

    @Test fun `unbound changes not applied`() = runTest {
        bind(watchable2, watchable1)
        for (i in 0 until 100) {
            watchable1.use { modify() }
        }
        watchable1.use(finalMod)

        waitFor(watchable2) { it == watchable2 }

        watchable2.unbind()
        watchable2.unbind() // Safe

        for (i in 0 until 100) {
            watchable1.use { modify() }
        }
        triggerActions()
        assertNotEquals(watchable1, watchable2)
    }

    @Test fun `batch changes together`() = runTest {
        val batches = mutableListOf<List<C>>()

        // Provide a non-0 batch amount
        watchable1.batch(this, 50) { batches += it }

        // Throw many modifications at watchable1
        for (i in 0 until 100) watchable1.use { modify() }

        triggerActions()
        assertEquals(listOf<List<C>>(), batches)

        advanceTimeBy(50)
        val size = batches.size
        assertNotEquals(0, size)

        advanceTimeBy(50)
        assertEquals(size, batches.size)
    }

    @Test fun equality() = runTest {
        bind(watchable2, watchable1)
        withTimeout(250) {
            waitFor(watchable2) { it == watchable1 }
        }
        assertEquals(watchable1, watchable1)
        assertEquals(watchable1, watchable2)
        assertEquals(watchable2, watchable1)
        watchable1.use { assertEquals(this, watchable2)}
    }

    @Test fun stress() = runBlocking(Dispatchers.Default) {
        val start = System.currentTimeMillis()
        val count = 1000

        bind(watchable2, watchable1)

        log("Launching $count modification jobs while bound")
        val allJobs = (0 until count).map {
            launch {
                watchable1.use {
                    modify()
                }
            }
        }

        watch(watchable2) {
            // Randomly read while modifying
            try {
                if (0 == chooser(10)) watchable2.toString()
            } catch (e: Exception) {
                log("THIS IS A PROBLEM: $e")
            }
        }

        // Wait for all modifications to be complete
        allJobs.joinAll()
        watchable1.use { finalMod(this) }

        // Eventually w2 will catch up to w1
        log("Waiting for everything to match")
        eventually(timeout = 5000) {
            assertEquals(watchable1, watchable2)
        }
        val elapsed = System.currentTimeMillis() - start
        log("$count in $elapsed ms. ${elapsed * 1000 / count} μs per iteration.")
    }

    companion object {
        private const val MAX_SIZE = 1000

        private val setModificationMaker: (Set<Int>, Chooser) -> ((MutableSet<Int>) -> Unit) = { set, chooser ->
            val value = chooser(MAX_SIZE)
            if (set.isEmpty()) { { mutable -> mutable.add(value)} } else when (chooser(3)) {
                0 -> { // Remove
                    val target = chooser(set);
                    { mutable -> mutable.remove(target) }
                }
                else -> { // Add
                    { mutable -> mutable.add(value) }
                }
            }
        }

        private val mapModificationMaker: (Map<Int, String>, Chooser) -> ((MutableMap<Int, String>) -> Unit) = { map, chooser ->
            // Add, remove, update
            when (chooser(4)) {
                0-> { // Randomly remove (if any)
                    val target = chooser(map.keys) ?: 0
                    { it.remove(target) }
                }
                1-> { // Select existing and modify (or add if none)
                    val target = chooser(map.keys) ?: 0
                    { it[target] = it[target]?.run { "$this." } ?: target.toString() }
                }
                else -> { // Add (or modify)
                    val target = chooser(MAX_SIZE);
                    { it[target] = it[target]?.run { "$this." } ?: target.toString() }
                }
            }
        }

        private val listModificationMaker: (List<Int>, Chooser) -> ((MutableList<Int>) -> Unit) = { list, chooser ->
            // Add, remove, update
            val value = chooser(MAX_SIZE)
            // If list is empty, add, otherwise return a random mutation
            if (list.isEmpty()) { { mutable -> mutable.add(value) } } else when (chooser(4)) {
                0-> { // Remove
                    val index = chooser(list.size);
                    { mutable -> mutable.removeAt(index) }
                }
                1-> { // Modify
                    val index = chooser(list.size);
                    { mutable -> mutable[index] = value }
                }
                else -> { // Add
                    { mutable -> mutable.add(value) }
                }
            }
        }

        @Parameters(name = "{0}") @JvmStatic fun parameters() = listOf(
            arrayOf(
                "WatchableMap",
                { watchableMapOf(111 to "111") },
                { watchableMapOf(222 to "222") },
                mapModificationMaker,
                { map: MutableMap<Int, String> -> map[MAX_SIZE] = MAX_SIZE.toString() }),
            arrayOf(
                "WatchableList",
                { watchableListOf(111) },
                { watchableListOf(222) },
                listModificationMaker,
                { list: MutableList<Int> -> list += MAX_SIZE }),
            arrayOf(
                "WatchableSet",
                { watchableSetOf(111) },
                { watchableSetOf(222) },
                setModificationMaker,
                { set: MutableSet<Int> -> set += MAX_SIZE }))
    }
}
