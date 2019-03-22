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
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WatchableListTest : ScopeTest() {
    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test fun clear() {
        runBlocking {
            val list = watchableListOf(3, 4)
            batch(list) { changes += it }
            changes.expect(ListChange.Initial(listOf(3, 4)))
            list.use { clear() }
            changes.expect(ListChange.Remove(0, 3), ListChange.Remove(0, 4))
            changes.expectNone()
        }
    }

    @Test fun readOnly() {
        runBlocking {
            val list = watchableListOf(1)
            val readOnlyList = list.readOnly()
            watch(readOnlyList) { changes += it }
            changes.expect(ListChange.Initial(listOf(1)))
            assertThat(list.toString(), startsWith("WatchableList("))
            assertThat(readOnlyList.toString(), startsWith("ReadOnlyWatchableList("))
        }
    }

    @Test fun withNull() {
        runBlocking {
            val list = watchableListOf(1, null)
            val channel = Channel<ListChange<Int?>>(20)
            watch(list) { channel.send(it) }
            channel.expect(ListChange.Initial(listOf(1, null)))
            list.use { remove(null) }
            channel.expect(ListChange.Remove(1, null))
        }
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
}
