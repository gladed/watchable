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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchableSetTest {
    val changes = Channel<SetChange<Int>>(Channel.UNLIMITED)

    @Test
    fun readOnly() = runBlocking {
        val set = watchableSetOf(1)
        val set2 = set.readOnly()
        watch(set2) { changes.send(it) }
        set.add(3)
        eventually { assertEquals(setOf(1, 3), set2) }
    }

    @Test
    fun listApis() {
        val set = watchableSetOf(1, 2)
        assertEquals(2, set.size)
        assertTrue(set.contains(2))
        assertFalse(set.containsAll(listOf(2, 3)))
        assertFalse(set.isEmpty())
        assertEquals(1, set.iterator().next())
    }

    @Test fun `add and remove`() = runBlocking {
        val set = watchableSetOf(1, null, 2)
        assertFalse(set.add(null))
        assertTrue(set.remove(null))
        assertFalse(set.remove(null))
        set.clear()
        assertTrue(set.isEmpty())
    }

    @Test fun `addAll etc`() = runBlocking {
        val set = watchableSetOf(1, 2)
        set.addAll(setOf(3, 4))
        set.addAll(arrayOf(5, 6))
        set.addAll(sequenceOf(7, 8))
        set.addAll(sequenceOf(9, 10).asIterable())
        set.removeAll(setOf(6, 7))
        set.retainAll(setOf(1, 3, 5, 7, 8, 9))
        assertEquals(setOf(1, 3, 5, 8, 9), set)
        Unit
    }

    @Test fun setOperators() = runTest {
        val set = watchableSetOf(1, 2)
        set += 3
        set += setOf(4, 5)
        set += arrayOf(6, 7)
        set += sequenceOf(8, 9)
        set -= 1
        set -= setOf(3)
        set -= arrayOf(5)
        set -= sequenceOf(7, 9)
        assertEquals(setOf(2, 4, 6, 8), set)

        set.retainAll(setOf(4, 6, 7))
        assertEquals(setOf(4, 6), set)
    }

}
