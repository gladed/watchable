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
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GroupTest {
    @Rule @JvmField val changes = ChangeWatcherRule<GroupChange>()

    @Test fun coverage() {
        val intValue = watchableValueOf(1)
        cover(GroupChange(intValue, ValueChange(1, 1)))
    }

    @Test(timeout = 500) fun `subscribe to a group of watchables`() {
        runBlocking {
            val intValue = watchableValueOf(1)
            val setValue = watchableSetOf("1")
            val rx = group(intValue, setValue).subscribe(this).receiver
            assertEquals(listOf(GroupChange(intValue, ValueChange(1, 1))), rx.receive())
            assertEquals(listOf(GroupChange(setValue, SetChange.Initial(setOf("1")))), rx.receive())
        }
    }

    @Test(timeout = 500) fun `watch a group of watchables`() {
        runBlocking {
            val intValue = watchableValueOf(1)
            val setValue = watchableSetOf("1")
            watch(group(intValue, setValue)) {
                changes += it
            }
            changes.expect(
                GroupChange(intValue, ValueChange(1, 1)),
                GroupChange(setValue, SetChange.Initial(setOf("1"))))
        }
    }
}
