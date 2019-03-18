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
import io.gladed.watchable.batch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class BatchTest : ScopeTest() {
    @Rule @JvmField val scope1 = ScopeRule(Dispatchers.Default)
    @Rule @JvmField val changes = ChangeWatcherRule<SetChange<Int>>()
    private val set = watchableSetOf(1)

    @Test fun `batch changes arrive slowly`() {
        runBlocking {
            batch(set, 150) {
                changes += it
            }
            changes.expect(SetChange.Initial(setOf(1)))

            set.use { add(2) }
            // Nothing shows up immediately
            changes.expectNone(25)
            changes.expect(SetChange.Add(2), timeout = 250)
        }
    }
}