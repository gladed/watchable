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
import io.gladed.watchable.bind
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

class WatchableSetTest : ScopeTest() {
    val changes = Channel<SetChange<Int>>(Channel.UNLIMITED)

    @Test
    fun replace() = runBlocking {
        val set = watchableSetOf(1)
        val set2 = watchableSetOf<Int>()
        bind(set2, set)
        val set3 = set2.readOnly()
        assertThat(set.toString(), startsWith("WatchableSet("))
        assertThat(set3.toString(), startsWith("ReadOnlyWatchableSet("))
        set.set(setOf(3))
        eventually { assertEquals(setOf(3), set3.value) }
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

    @Test
    fun `slow bind`() = runBlocking {
        val set = watchableSetOf(1)
        val set2 = watchableSetOf(2)
        watch(set) { changes.send(it) }
        changes.expect(SetChange.Initial(setOf(1)))
        bind(set2, set)
        set.set(setOf(3))
        set.use { add(2) }
        changes.expect(SetChange.Remove(1), SetChange.Add(3), SetChange.Add(2))
    }
}
