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
import io.gladed.watchable.group
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.watch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

/** Check certain doc comments are accurate. */
class ReadmeTest {
    @Test fun `batching docs`() {
        val list = listOf(4, 5).toWatchableList()
        runBlocking {
            batch(list) { println(it) }
            delay(25)
            // Prints: [Initial(initial=[4, 5])]
            list.use {
                add(6)
                add(7)
            }
            delay(25)
            // Prints: [Add(index=2, added=6), Add(index=3, added=7)]
        }
    }

    @Test fun `grouping docs`() {
        val list = listOf(4).toWatchableList()
        val set = setOf("a").toWatchableSet()
        runBlocking {
            watch(group(set, list)) { println(it) }
            delay(25)
            // Prints:
            //   GroupChange(watchable=WatchableSet(), change=Initial(initial=[a]))
            //   GroupChange(watchable=WatchableList(), change=Initial(initial=[4]))
            list.use { add(6) }
            set.use { add("b") }
            //   GroupChange(watchable=WatchableList(), change=Add(index=1, added=6))
            //   GroupChange(watchable=WatchableSet(), change=Add(added=b))
            delay(25)
        }
    }
}
