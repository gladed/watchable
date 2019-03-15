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
import io.gladed.watchable.ListChange
import io.gladed.watchable.ValueChange
import io.gladed.watchable.batch
import io.gladed.watchable.subscribe
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FlushTest : ScopeTest() {
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test(timeout = 500)
    fun `receive events while flushing`() {
        val value = watchableValueOf(1)
        val handle = subscribe(value)
        runBlocking {
            assertEquals(listOf(ValueChange(1, 1)), handle.receive())
            value.set(2)
            handle.close()
            assertEquals(listOf(ValueChange(1, 2)), handle.receive())
            handle.join()
        }
    }


    @Test(timeout = 500)
    fun `get final batch`() {
        runBlocking {
            val value = watchableValueOf(1)
            val handle = batch(value) { changes += it }
            changes.expect(ValueChange(1, 1))
            value.set(2)
            handle.close()
            changes.expect(ValueChange(1, 2))
            handle.join()
        }
    }
}
