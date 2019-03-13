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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class OverloadTest {
    @Rule
    @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test
    fun manyChanges() {
        runBlocking {
            val list = watchableListOf(1)
            watch(list) {
                if (it is ListChange.Add && (it.added % 2 == 0)) {
                    // If we could `use` here we could cause a deadlock. But we can't because
                    // `watch` takes a non-suspending lambda. So we must launch, which keeps things rolling.
                    launch {
                        list.use {
                            remove(it.added)
                        }
                    }
                }
                changes += it
            }
            changes.expect(ListChange.Initial(listOf(1)))

            // Generate more changes than will fit in the channel at once
            val range = 0 until 20
            range.forEach { value ->
                list.use {
                    log("Adding $value")
                    add(value)
                }
            }

            // Wait until 18 (the last even item) is removed
            changes.expect(ListChange.Remove(10, 18), strict = false)
        }
    }
}
