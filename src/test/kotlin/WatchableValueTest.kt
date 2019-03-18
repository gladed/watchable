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
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.watch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class WatchableValueTest {

    private lateinit var intValue: WatchableValue<Int>
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun simple() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) {
                log("Updating received with $it, was ${it.oldValue}")
                changes += it
            }
            log("Waiting for value change")
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))
            log("At end, shutting down")
        }
    }

    @Test fun setSameValue() {
        runBlocking {
            intValue = 5.toWatchableValue()
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(5)
            // Both announcements because value is NOT compared for equality
            changes.expect(ValueChange(5, 5))
        }
    }

    @Test fun watchUnmodifiable() {
        runBlocking {
            intValue = watchableValueOf(4)
            val readOnly = intValue.readOnly()
            assertThat(readOnly.toString(), startsWith("ReadOnlyWatchableValue("))
            assertThat(intValue.toString(), startsWith("WatchableValue("))
            watch(readOnly) {
                changes += it
            }
            changes.expect(ValueChange(4, 4))
            intValue.set(5)
            assertEquals(5, readOnly.value)
            intValue.set(6)
            assertEquals(6, readOnly.value)
            changes.expect(ValueChange(4, 5))
            changes.expect(ValueChange(5, 6))
            assertEquals(6, readOnly.value)
        }
    }
}
