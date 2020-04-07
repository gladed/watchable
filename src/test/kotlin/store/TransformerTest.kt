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

package store

import impossible
import io.gladed.watchable.store.MemoryStore
import io.gladed.watchable.store.Transformer
import io.gladed.watchable.store.transform
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Test
import runTest

class TransformerTest {
    private val stringToInt = object : Transformer<String, Int> {
        override fun toTarget(value: String): Int = value.toInt()
        override fun fromTarget(value: Int): String = value.toString()
    }

    private val intToString = object : Transformer<Int, String> {
        override fun toTarget(value: Int): String = value.toString()
        override fun fromTarget(value: String): Int = value.toInt()
    }

    @Test fun cycle() {
        assertEquals(5, (intToString + stringToInt).toTarget(5))
        assertEquals(5, (intToString + stringToInt).fromTarget(5))
    }

    @Test fun inflateStore() = runTest {
        val memory = MemoryStore<Int>("int")
        val inflated = memory.transform(intToString)
        inflated.put(":1", "1")
        assertEquals("1", inflated.get(":1"))
        assertEquals(listOf(":1"), inflated.keys().toList())
        inflated.remove(":1")
        impossible {
            memory.get(":1")
        }
    }
}
