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
import io.gladed.watchable.subscribe
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FlushTest : ScopeTest() {
    @Test(timeout = 250)
    fun `receive events while flushing`() {
        runBlocking {
            val value = watchableValueOf(1)
            val handle = subscribe(value)
            assertEquals(listOf(ValueChange(1, 1)), handle.receive())
            value.set(2)
            handle.close()
            assertEquals(listOf(ValueChange(1, 2)), handle.receive())
            handle.join()
        }
    }
}
