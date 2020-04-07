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
package util

import io.gladed.watchable.store.cannot
import io.gladed.watchable.util.Try
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TryTest {
    @Test fun `successful try`() = runBlockingTest {
        val tried = Try { 5 }
        assertEquals(5, tried.passOrNull)
        assertNull(tried.failOrNull)
    }

    @Test fun `unsuccessful try`() = runBlockingTest {
        val tried = Try<Int> { cannot("get it") }
        assertEquals("get it", tried.failOrNull?.message)
        assertNull(tried.passOrNull)
    }
}
