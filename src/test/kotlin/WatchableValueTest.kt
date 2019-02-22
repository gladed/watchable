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

import com.gladed.watchable.LocalScope
import com.gladed.watchable.ValueChange
import com.gladed.watchable.WatchableValue
import com.gladed.watchable.watch
import com.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Executors

class WatchableValueTest {
    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "single-threaded")
    }.asCoroutineDispatcher()

    private val scope = LocalScope(dispatcher)

    private lateinit var intValue: WatchableValue<Int>

    @Test fun watchTest() {
        var received = -1
        runThenCancel {
            intValue = watchableValueOf(5)
            watch(intValue) {
                log("Updating received with $it")
                received = it.newValue
            }
            yield() // Yield to allow initial callback
            assertEquals(5, received)
            intValue.value = 17
            yield()
            assertTrue(intValue.isActive())
        }
        assertEquals(17, received)

        intValue.value = 88
        assertFalse(intValue.isActive())
        runBlocking {
            delay(50)
            assertEquals(17, received) // Scope closed so no more updates
        }
    }

    @Test fun setSameValue() {
        val received = mutableListOf<ValueChange<Int>>()
        runThenCancel {
            intValue = watchableValueOf(5)
            watch(intValue) {
                received.add(it)
            }
            intValue.value = 5
            yield()
        }
        // Only one announcement
        assertEquals(listOf(5), received.map { it.newValue })
        println(intValue)
    }

    @Test fun watchUnmodifiable() {
        val received = mutableListOf<ValueChange<Int>>()
        runThenCancel {
            intValue = watchableValueOf(5)
            val readOnly = intValue.readOnly()
            println("Object: $readOnly") // Coverage
            watch(readOnly) {
                received.add(it)
            }
            intValue.value = 6
            assertEquals(6, readOnly.value)
            yield()
        }
        println(received)
        assertEquals(listOf(5, 6), received.map { it.newValue })
    }

    @Test fun watchFromOtherScope() {
        var received = -1
        runBlocking {
            intValue = scope.watchableValueOf(5)
            watch(intValue) {
                log("received $it")
                received = it.newValue
            }
            delay(50)
            assertEquals(5, received)
            intValue.value = 17
            delay(50)
            assertEquals(17, received)

            // Shut down the other scope
            scope.coroutineContext.cancel()
            assertFalse(intValue.isActive())
            intValue.value = 88
            delay(50)
        }
        assertEquals(17, received) // Scope closed so no more updates
    }

    @Test fun watchOnOtherScope() {
        var received = -1
        runThenCancel {
            intValue = watchableValueOf(5)
            scope.watch(intValue) {
                received = it.newValue
            }
            delay(50)
            assertEquals(5, received)
            intValue.value = 17
            delay(50)
            assertEquals(17, received)

            // Shut down the other scope
            scope.coroutineContext.cancel()
            intValue.value = 88
            delay(50)
        }
        assertEquals(17, received) // Scope closed so there will have been no more updates
        assertFalse(intValue.isActive())
    }

    companion object {
        private fun log(message: String) {
            println(Thread.currentThread().name + ": $message")
        }
    }
}
