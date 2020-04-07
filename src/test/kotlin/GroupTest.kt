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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupTest {
    val changes = Channel<GroupChange>(Channel.UNLIMITED)

    @Test fun coverage() {
        val intValue = watchableValueOf(1)
        cover(GroupChange(intValue, ValueChange(null, 1, true)))
    }

    @Test fun `subscribe to a group of watchables`() = runTest {
        val intValue = watchableValueOf(1)
        val setValue = watchableSetOf("1")
        group(intValue, setValue).watch(this) { changes.send(it) }
        val initialList = listOf(changes.receive(), changes.receive())
        assertEquals(listOf(
            GroupChange(intValue, ValueChange(null, 1, true)),
            GroupChange(setValue, SetChange.Initial(setOf("1")))), initialList)
        assertEquals(listOf(true, true), initialList.map { it.isInitial })
    }

    @Test fun `watch a group of watchables`() = runTest {
        val intValue = watchableValueOf(1)
        val setValue = watchableSetOf("1")
        watch(group(intValue, setValue)) { changes.send(it) }
        changes.mustBe(
            GroupChange(intValue, ValueChange(null, 1, true)),
            GroupChange(setValue, SetChange.Initial(setOf("1"))))
    }

    @Test fun `close watching of a group`() = runTest {
        val intValue = watchableValueOf(1)
        val setValue = watchableSetOf("1")
        val handle = watch(group(intValue, setValue)) { changes.send(it) }
        changes.mustBe(
            GroupChange(intValue, ValueChange(null, 1, true)),
            GroupChange(setValue, SetChange.Initial(setOf("1"))))
        handle.stop()
        changes.mustBe()
        setValue { add("2") }
        changes.mustBe()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `cancel watching of a group`() = runTest {
        val intValue = watchableValueOf(1)
        val setValue = watchableSetOf("1")
        pauseDispatcher {
            val handle = watch(group(intValue, setValue)) { changes.send(it) }
            handle.cancel()
        }
        changes.mustBe()
        setValue { add("2") }
        changes.mustBe()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `start a group of watchables`() = runTest {
        val intValue = watchableValueOf(1)
        pauseDispatcher {
            val watcher = watch(group(intValue)) { changes.send(it) }
            changes.mustBe()
            watcher.start()
            changes.mustBe(GroupChange(intValue, ValueChange(null, 1, isInitial = true)))
        }
    }
}
