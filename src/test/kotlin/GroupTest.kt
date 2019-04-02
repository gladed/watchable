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

import io.gladed.watchable.GroupChange
import io.gladed.watchable.SetChange
import io.gladed.watchable.ValueChange
import io.gladed.watchable.group
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GroupTest {
    val changes = Channel<GroupChange<Any, Any>>(Channel.UNLIMITED)

    @Test fun coverage() {
        val intValue = watchableValueOf(1)
        cover(GroupChange(intValue, ValueChange(1)))
    }

    @Test fun `subscribe to a group of watchables`() {
        runBlocking {
            val intValue = watchableValueOf(1)
            val setValue = watchableSetOf("1")
            group(intValue, setValue).watch(this) { changes.send(it) }
            changes.expect(GroupChange(intValue, ValueChange(1)))
            changes.expect(GroupChange(setValue, SetChange.Add(listOf("1"))))
        }
    }

    @Test fun `watch a group of watchables`() {
        runBlocking {
            val intValue = watchableValueOf(1)
            val setValue = watchableSetOf("1")
            watch(group(intValue, setValue)) { changes.send(it) }
            changes.expect(
                GroupChange(intValue, ValueChange(1)),
                GroupChange(setValue, SetChange.Add(listOf("1"))))
        }
    }

    @Test fun `cancel watching of a group`() {
        runBlocking {
            val intValue = watchableValueOf(1)
            val setValue = watchableSetOf("1")
            val handle = watch(group(intValue, setValue)) { changes.send(it) }
            changes.expect(
                GroupChange(intValue, ValueChange(1)),
                GroupChange(setValue, SetChange.Add(listOf("1"))))
            handle.close()
            changes.expectNone()
            setValue.use { add("2") }
            changes.expectNone()
        }
    }

    @Test fun `example from readme`() {
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
