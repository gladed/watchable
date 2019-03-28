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
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/** Check certain doc comments are accurate. */
class ReadmeTest {
    val out = mutableListOf<String>()

    @Test fun `batching docs`() = runBlocking {
        val list = listOf(4, 5).toWatchableList()
        batch(list) { out += it.toString() }
        delay(25)
        list.use {
            add(6)
            add(7)
        }
        delay(25)
        assertEquals("""
            [Initial(initial=[4, 5])]
            [Add(index=2, added=6), Add(index=3, added=7)]""".trimIndent(), out.joinToString("\n"))

        // Prints: [Add(index=2, added=6), Add(index=3, added=7)]
    }

    @Test fun `close watch handle`() = runBlocking {
        val list = watchableListOf(1)
        val handle = watch(list) { out += it.toString() }
        delay(10)
        list.add(2)
        handle.closeAndJoin()
        list.add(3)
        delay(10)
        assertEquals("""
            Initial(initial=[1])
            Add(index=1, added=2)""".trimIndent(), out.joinToString("\n"))
    }
}
