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

import io.gladed.watchable.ValueChange
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.batch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class BatchTest {
    private lateinit var intValue: WatchableValue<Int>
    @Rule
    @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun batch() {
        runBlocking {
            intValue = watchableValueOf(1)
            batch(intValue, 50) {
                changes += it
            }
            changes.expect(ValueChange(1, 1)) // Get situated
            intValue.set(10)
            intValue.set(20)
            intValue.set(30)
            delay(25)
            changes.mustHaveLists(listOf(ValueChange(1, 1))) // The first list ONLY
            delay(60)
            changes.mustHaveLists(listOf(ValueChange(1, 10), ValueChange(10, 20), ValueChange(20, 30)))
        }
    }
}
