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

import io.gladed.watchable.bind
import io.gladed.watchable.twoWayBind
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import org.junit.Assert.assertEquals
import org.junit.Test

class TwoWayBindTest {
    private val set = watchableSetOf<String>()
    private val set2 = watchableSetOf<String>()
    private val map = watchableMapOf(1 to "1")

    @Test fun `complex bind`() = runTest {
        twoWayBind(set, map, { mapChange ->
            mapChange.simple.forEach { change ->
                change.remove?.also { remove(it) }
                change.add?.also { add(it) }
            }
        }) { setChange ->
            setChange.simple.forEach { change ->
                change.remove?.also { remove(it.toInt()) }
                change.add?.also { put(it.toInt(), it) }
            }
        }
        assertEquals(setOf("1"), set)
        set { add("2") }
        assertEquals("2", map[2])
    }

    @Test fun `cannot rebind`() = runTest {
        twoWayBind(set, set2)

        mustThrow(IllegalStateException::class.java) { twoWayBind(set2, set) }

        mustThrow(IllegalStateException::class.java) { twoWayBind(set, set2) }
    }

    @Test
    fun `can bind from outside of two-way bind`() = runTest {
        val set3 = watchableSetOf("2")
        twoWayBind(set, set2)
        bind(set3, set)
        set2.add("1")
        assertEquals(setOf("1"), set3)
    }

    @Test fun `simple bind between sets`() = runTest {
        set2.add("1")
        twoWayBind(set, set2)
        set.add("2")
        set2.add("3")

        assertEquals(setOf("1", "2", "3"), set)
        assertEquals(setOf("1", "2", "3"), set2)
    }

    @Test fun `unbind both with either one`() = runTest {
        twoWayBind(set, set2)
        set.unbind()
        set.add("1")
        set2.add("2")

        assertEquals(setOf("1"), set)
        assertEquals(setOf("2"), set2)
    }
}
