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

import io.gladed.watchable.MapChange
import io.gladed.watchable.bind
import io.gladed.watchable.waitFor
import io.gladed.watchable.watch
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

class WatchableMapTest : ScopeTest() {
    val changes = Channel<MapChange<Int, String>>(Channel.UNLIMITED)

    @Test fun entries() = runBlocking {
        val map = watchableMapOf(1 to "1")
        map.use {
            val first = entries.first()
            assertEquals(first, first)
            log("Entry: $entries has hash ${entries.hashCode()}") // Coverage
            mustThrow(UnsupportedOperationException::class.java) {
                entries.add(object : MutableMap.MutableEntry<Int, String> {
                    override val key: Int = 1
                    override val value: String = "1"
                    override fun setValue(newValue: String) = throw Error()
                })
            }
            first.setValue("2")
        }
        assertEquals(mapOf(1 to "2"), map)
    }

    @Test fun readOnly() = runBlocking {
        val map = watchableMapOf(1 to "1")
        val map2 = watchableMapOf(2 to "2")
        val map3 = map2.readOnly()
        watch(map3) { log(it); changes.send(it) }
        changes.expect(MapChange.Initial(mapOf(2 to "2")))
        map2.bind(this, map)
        changes.expect(MapChange.Remove(2, "2"), MapChange.Put(1, null, "1"))
    }

    @Test fun bindReadOnly() = runBlocking {
        val map = watchableMapOf(1 to "1")
        val map2 = watchableMapOf(2 to "2")
        val map3 = map.readOnly()
        bind(map2, map3)
        map.put(3, "3")

        waitFor(map) { it == map3 }
    }

    @Test fun `put and remove`() = runBlocking {
        val map = watchableMapOf(1 to "1", 2 to "2")
        assertEquals("1", map.put(1, "11"))
        assertEquals("11", map.remove(1))
        map.clear()
        assertTrue(map.isEmpty())
    }

    @Test fun `putAll cases`() = runBlocking {
        val map = watchableMapOf(1 to "1", 2 to "2")
        map.putAll(listOf(3 to "3"))
        map.putAll(arrayOf(4 to "4"))
        map.putAll(sequenceOf(5 to "5"))
        map.putAll(mapOf(6 to "6"))
        map.forEach { entry ->
            assertEquals(entry.key.toString(), entry.value)
        }
        assertEquals("123456", map.values.joinToString(""))
    }

    @Test fun listApis() {
        val map = watchableMapOf(1 to "1", 2 to "2")
        assertEquals(2, map.size)
        assertEquals("1", map.entries.first().value)
        assertEquals(1, map.keys.first())
        assertEquals("1", map.values.first())
        assertTrue(map.containsKey(2))
        assertTrue(map.containsValue("1"))
        assertEquals("2", map[2])
        assertFalse(map.isEmpty())
    }

    @Test fun mapOperators() = runTest {
        val map = watchableMapOf(1 to "1", 2 to "2")
        map += 3 to "3"
        map += mapOf(4 to "4", 5 to "5")
        map += arrayOf(6 to "6", 7 to "7")
        map += sequenceOf(8 to "8", 9 to "9")
        map -= 1
        map -= setOf(3)
        map -= arrayOf(5)
        map -= sequenceOf(7, 9)
        assertEquals(mapOf(2 to "2", 4 to "4", 6 to "6", 8 to "8"), map)
    }
}
