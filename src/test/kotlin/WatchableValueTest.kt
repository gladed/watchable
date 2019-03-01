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
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
            intValue.watch {
                log("Updating received with $it, was ${it.oldValue}")
                received = it.newValue
            }
            delay(50)
            assertEquals(5, received)
            intValue.set(17)
            delay(50)
            assertTrue(intValue.isActive())
        }
        assertEquals(17, received)

        runBlocking {
            intValue.set(88)
        }
        assertFalse(intValue.isActive())
        runBlocking {
            delay(50)
            assertEquals(17, received) // Scope closed so no more updates
        }
    }

    @Test fun noFreeze() {
        try {
            runBlocking {
                val value = watchableValueOf(5)
                value.watch { }
                // The only way out. If we don't throw, runBlocking halts forever waiting for the internal
                // channel to close.
                throw Error("must throw")
            }
        } catch (e: Error) { }
    }

    @Test fun setSameValue() {
        val received = mutableListOf<ValueChange<Int>>()
        runThenCancel {
            intValue = watchableValueOf(5)
            intValue.watch {
                received.add(it)
            }
            intValue.set(5)
            delay(50)
            // Both announcements, value is NOT compared for equality
            assertEquals(listOf(5, 5), received.map { it.newValue })
        }
        println(intValue)
    }

    @Test fun watchUnmodifiable() {
        val received = mutableListOf<ValueChange<Int>>()
        runThenCancel {
            intValue = watchableValueOf(5)
            val readOnly = intValue.readOnly()
            println("Object: $readOnly") // Coverage
            readOnly.watch {
                received.add(it)
            }
            intValue.set(6)
            assertEquals(6, readOnly.get())
            delay(50)
        }
        println(received)
        assertEquals(listOf(5, 6), received.map { it.newValue })
    }

    @Test fun watchFromOtherScope() {
        runBlocking {
            var received = -1
            intValue = scope.watchableValueOf(5)
            intValue.watch {
                log("received $it")
                received = it.newValue
            }
            delay(50)
            assertEquals(5, received)
            intValue.set(17)
            delay(50)
            assertEquals(17, received)

            // Shut down the other scope
            scope.coroutineContext.cancel()
            assertFalse(intValue.isActive())
            delay(50)
            intValue.set(88)
            delay(50)

            assertEquals(17, received) // Scope closed so no more updates
        }
    }

    @Test fun watchOnOtherScope() {
        var received = -1
        runThenCancel {
            intValue = watchableValueOf(5)
            scope.launch {
                intValue.watch {
                    received = it.newValue
                }
            }
            delay(50)
            assertEquals(5, received)
            intValue.set(17)
            delay(50)
            assertEquals(17, received)

            // Shut down the other scope
            scope.coroutineContext.cancel()
            intValue.set(88)
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
