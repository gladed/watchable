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
import io.gladed.watchable.batch
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchableListTest {
    val changes = Channel<Any>(Channel.UNLIMITED)

    @Test fun clear() = runTest {
        val list = watchableListOf(3, 4)
        batch(list) { changes.send(it) }
        changes.mustBe(listOf(ListChange.Initial(listOf(3, 4))))
        list { clear() }
        changes.mustBe(listOf(ListChange.Remove(0, 3), ListChange.Remove(0, 4)))
        changes.mustBe()
    }

    @Test fun readOnly() = runTest {
        val list = watchableListOf(1)
        val readOnlyList = list.readOnly()
        watch(readOnlyList) { changes.send(it) }
        changes.mustBe(ListChange.Initial(listOf(1)))
    }

    @Test fun withNull() = runTest {
        val list = watchableListOf(1, null)
        val channel = Channel<ListChange<Int?>>(20)
        watch(list) { channel.send(it) }
        channel.mustBe(ListChange.Initial(listOf(1, null)))
        list { remove(null) }
        channel.mustBe(ListChange.Remove(1, null))
    }

    @Test fun `equality with null`() {
        val list = watchableListOf(1, null, 2)
        assertEquals(listOf(1, null, 2), list)
        assertEquals(list, listOf(1, null, 2))
    }

    @Test fun `hashcode with null`() {
        val list = watchableListOf(1, null, 2)
        assertEquals(listOf(1, null, 2).hashCode(), list.hashCode())
    }

    @Test fun `add and remove`() = runTest {
        val list = watchableListOf(1, null, 2)
        list.add(null)
        assertTrue(list.remove(null))
        assertTrue(list.remove(null))
        assertFalse(list.remove(null))
        list.clear()
        assertTrue(list.isEmpty())
    }

    @Test fun `addAll etc`() = runTest {
        val list = watchableListOf(1, 2)
        list.addAll(setOf(3, 4))
        list.addAll(arrayOf(5, 6))
        list.addAll(sequenceOf(7, 8))
        list.addAll(sequenceOf(9, 10).asIterable())
        list.removeAll(setOf(6, 7))
        list.retainAll(listOf(1, 3, 5, 7, 8, 9))
        assertEquals(listOf(1, 3, 5, 8, 9), list)
    }

    @Test fun listApis() {
        val list = watchableListOf(1, 2)
        assertEquals(2, list.size)
        assertTrue(list.contains(2))
        assertFalse(list.containsAll(setOf(3, 2)))
        assertEquals(1, list[0])
        assertEquals(1, list.indexOf(2))
        assertFalse(list.isEmpty())
        assertEquals(-1, list.lastIndexOf(3))
        assertEquals(1, list.iterator().next())
        assertEquals(0, list.listIterator().nextIndex())
        assertEquals(2, list.listIterator(1).next())
        assertEquals(listOf(2), list.subList(1, 2))
    }

    @Test fun listOperators() = runTest {
        val list = watchableListOf(1, 2)
        list += 3
        list += listOf(4, 5)
        list += arrayOf(6, 7)
        list += sequenceOf(8, 9)
        list -= 1
        list -= listOf(3)
        list -= arrayOf(5)
        list -= sequenceOf(7, 9)
        assertEquals(listOf(2, 4, 6, 8), list)

        list.retainAll(listOf(4, 6, 7))
        assertEquals(listOf(4, 6), list)
    }
}
