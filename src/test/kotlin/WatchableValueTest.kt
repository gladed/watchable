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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThat
import org.junit.Test

class WatchableValueTest {

    private lateinit var intValue: WatchableValue<Int>
    val changes = Channel<ValueChange<Int>>(Channel.UNLIMITED)

    @Test fun simple() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) {
                changes.send(it)
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))
        }
    }

    @Test fun setSameValue() {
        runBlocking {
            intValue = 5.toWatchableValue()
            watch(intValue) { changes.send(it) }
            changes.expect(ValueChange(5, 5))
            intValue.set(5)
            // Both announcements because value is NOT compared for equality
            changes.expect(ValueChange(5, 5))
        }
    }

    @Test fun `equality with similar`() {
        val watchable1 = 1.toWatchableValue()
        val watchable2 = 1.toWatchableValue()
        assertEquals(watchable1, watchable2)
    }

    @Test fun `equality with null`() {
        val watchable = (null as Int?).toWatchableValue()
        assertEquals(watchable, null)
        assertNotEquals(null, watchable) // Unfortunately value tests cannot be symmetric
        assertEquals(watchable.hashCode(), null.hashCode())
    }

    @Test fun `equality and hash`() {
        val watchable = 5.toWatchableValue()
        assertEquals(watchable, 5)
        assertNotEquals(5, watchable) // Unfortunately value tests cannot be symmetric
        assertEquals(watchable.hashCode(), 5.hashCode())
    }

    @Test fun watchUnmodifiable() {
        runBlocking {
            intValue = watchableValueOf(4)
            val readOnly = intValue.readOnly()
            assertThat(readOnly.toString(), startsWith("ReadOnlyWatchableValue("))
            assertThat(intValue.toString(), startsWith("WatchableValue("))
            watch(readOnly) {
                changes.send(it)
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
