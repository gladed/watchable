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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.delay
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
class MatrixTest<T, V, M : T, C: Change>: ScopeTest() {

    private val chooser = Chooser()

    @Suppress("unused")
    @Parameter(0) lateinit var name: String
    @Parameter(1) lateinit var maker1: () -> MutableWatchable<T, V, M, C>
    @Parameter(2) lateinit var maker2: () -> MutableWatchable<T, V, M, C>
    @Parameter(3) lateinit var modificationMaker: (M, Chooser) -> ((M) -> Unit)
    @Parameter(4) lateinit var finalMod: (M) -> Unit


    private lateinit var watchable1: MutableWatchable<T, V, M, C>
    private lateinit var watchable2: MutableWatchable<T, V, M, C>

    /** Apply a random modification to this [M]. */
    private fun M.modify() {
        modificationMaker(this, chooser).invoke(this)
    }


    @Before fun setup() {
        watchable1 = maker1()
        watchable2 = maker2()
    }

    @Test fun `bind works`() = runBlocking {
        assertFalse(watchable2.isBound())
        bind(watchable2, watchable1)
        assertTrue(watchable2.isBound())
        watchable2.watchUntil(this) { assertEquals(watchable1, watchable2) }
    }

    @Test fun `bind then make changes`() = runBlocking {
        bind(watchable2, watchable1)
        for (i in 0 until 200) {
            watchable1.use { modify() }
        }
        assertNotEquals(watchable1, watchable2)
        watchable2.watchUntil(this) { assertEquals(watchable1, watchable2) }
        log(watchable1)
    }

    @Test fun `no change on bound`() = runBlocking {
        bind(watchable2, watchable1)
        try {
            watchable2.use { modify() }
            fail("Should not have worked")
        } catch (e: IllegalStateException) {
            log("Correct failure: $e")
        }
    }

    @Test fun `no self-bind`() = runBlocking {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable1, watchable1)
        }
    }

    @Test fun `no circular-bind`() = runBlocking {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable2, watchable1)
            bind(watchable1, watchable2)
        }
    }

    @Test fun `no write when bound`() = runBlocking {
        mustThrow(IllegalStateException::class.java) {
            bind(watchable2, watchable1)
            for (i in 0 until 20) {
                watchable2.use { modify() }
            }
        }
    }

    @Test fun `unbound changes not applied`() = runBlocking {
        withTimeout(1000) {
            bind(watchable2, watchable1)
            for (i in 0 until 100) {
                watchable1.use { modify() }
            }
            watchable1.use(finalMod)

            watchable2.watchUntil(this) {
                assertEquals(watchable1, watchable2)
            }

            watchable2.unbind()
            watchable2.unbind() // Safe

            for (i in 0 until 100) {
                watchable1.use { modify() }
            }
            delay(50) // No changes arrive
            assertNotEquals(watchable1, watchable2)
        }
    }

    @Test fun `batch changes together`() = runBlocking {
        val changes = mutableListOf<C>()
        val batches = mutableListOf<List<C>>()
        bind(watchable2, watchable1)

        log("watch()")

        // Timing output here proves that modifications are held up.
        // Provide a non-0 batch amount
        watchable1.watch(this) {
            changes += it
        }

        // Provide a non-0 batch amount
        watchable1.batch(this, 50) {
            batches += it
        }

        // The first batch should arrive quickly and contain initial
        eventually { assertTrue(batches.size > 0) }

        // Throw many modifications at watchable1
        for (i in 0 until 100) {
            watchable1.use {
                modify()
            }
        }

        // Wait for everything to arrive at watchable2
        watchable2.watchUntil(this) {
            assertEquals(watchable1, watchable2)
        }

        eventually {
            log("changes: ${changes.size}, batches: ${batches.size}")
            assertTrue(changes.size > 0)
            assertEquals(changes, batches.flatten())
            assertNotEquals(changes.size, batches.size)
        }
    }

    @Test fun equality() {
        assertEquals(watchable1.value, watchable1)
        assertEquals(watchable1, watchable1.value)
        assertNotEquals(watchable2.value, watchable1)
        assertNotEquals(watchable2, watchable1.value)
    }

    @Test fun replace() = runBlocking {
        watchable1.set(watchable2.value)
        assertEquals(watchable1, watchable2)
    }

    @Test fun stress() = runBlocking {
        val start = System.currentTimeMillis()
        val count = 2000
        bind(watchable2, watchable1)
        val allJobs = (0 until count).map {
            launch {
                watchable1.use {
                    modify()
                }
            }
        }

        allJobs.joinAll()
        watchable1.use {
            finalMod(this)
        }

        watch(watchable2) {
            // Randomly read while modifying
            try {
                if (0 == chooser(10)) watchable2.value.toString()
            } catch (e: Exception) {
                log("THIS IS A PROBLEM: $e")
            }
        }

        // Eventually w2 will catch up to w1
        watchable2.watchUntil(this) { assertEquals(watchable1, watchable2) }
        assertEquals(watchable1.hashCode(), watchable2.hashCode())
        val elapsed = System.currentTimeMillis() - start
        log("$count in $elapsed ms. ${elapsed * 1000 / count} μs per iteration.")
    }

    companion object {
        private const val MAX_SIZE = 1000

        private val setModificationMaker: (Set<Int>, Chooser) -> ((MutableSet<Int>) -> Unit) = { set, chooser ->
            val value = chooser(MAX_SIZE)
            if (set.isEmpty()) { { mutable -> mutable.add(value)} } else when (chooser(3)) {
                0 -> { // Add
                    { mutable -> mutable.add(value) }
                }
                else -> { // Remove
                    val target = chooser(set);
                    { mutable -> mutable.remove(target) }
                }
            }
        }

        private val mapModificationMaker: (Map<Int, String>, Chooser) -> ((MutableMap<Int, String>) -> Unit) = { map, chooser ->
            // Add, remove, update
            when (chooser(3)) {
                0-> { // Add (or modify)
                    val target = chooser(MAX_SIZE);
                    { it[target] = it[target]?.run { "$this." } ?: target.toString() }
                }
                1-> { // Select existing and modify (or add if none)
                    val target = chooser(map.keys) ?: 0
                    { it[target] = it[target]?.run { "$this." } ?: target.toString() }
                }
                else -> { // Randomly remove (if any)
                    val target = chooser(map.keys) ?: 0
                    { it.remove(target) }
                }
            }
        }

        private val listModificationMaker: (List<Int>, Chooser) -> ((MutableList<Int>) -> Unit) = { list, chooser ->
            // Add, remove, update
            val value = chooser(MAX_SIZE)
            // If list is empty, add, otherwise return a random mutation
            if (list.isEmpty()) { { mutable -> mutable.add(value) } } else when (chooser(3)) {
                0-> { // Add
                    { mutable -> mutable.add(value) }
                }
                1-> { // Modify
                    val index = chooser(list.size);
                    { mutable -> mutable[index] = value }
                }
                else -> { // Remove
                    val index = chooser(list.size);
                    { mutable -> mutable.removeAt(index) }
                }
            }
        }

        @Parameters(name = "{0}") @JvmStatic fun parameters() = listOf(
            arrayOf(
                "WatchableMap",
                { watchableMapOf(1 to "1") },
                { watchableMapOf(2 to "2") },
                mapModificationMaker,
                { map: MutableMap<Int, String> -> map[MAX_SIZE] = MAX_SIZE.toString() }),
            arrayOf(
                "WatchableList",
                { watchableListOf(1) },
                { watchableListOf(2) },
                listModificationMaker,
                { list: MutableList<Int> -> list += MAX_SIZE }),
            arrayOf(
                "WatchableSet",
                { watchableSetOf(1) },
                { watchableSetOf(2) },
                setModificationMaker,
                { set: MutableSet<Int> -> set += MAX_SIZE }))
    }
}
