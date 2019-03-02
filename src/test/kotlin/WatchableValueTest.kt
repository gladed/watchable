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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class WatchableValueTest {
    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "single-threaded")
    }.asCoroutineDispatcher()

    private val scope = LocalScope(dispatcher)

    private lateinit var intValue: WatchableValue<Int>
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun simple() {
        runThenCancel {
            intValue = watchableValueOf(5)
            watch(intValue) {
                log("Updating received with $it, was ${it.oldValue}")
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            assertTrue(intValue.isActive)
            changes.expect(ValueChange(5, 17))
        }
    }

    @Test fun noCallbackAfterScopeClose() {
        runThenCancel {
            intValue = watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
        }

        runThenCancel {
            // intValue can still be set
            intValue.set(88)
            assertEquals(88, intValue.get())
            // But, it's not active
            assertFalse(intValue.isActive)
            // And it generates no changes
            changes.expectNone()
        }
    }

    @Test fun noWatchAfterScopeClose() {
        runThenCancel {
            intValue = watchableValueOf(5)
        }

        try {
            runThenCancel {
                watch(intValue) { }
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun setSameValue() {
        runThenCancel {
            intValue = watchableValueOf(5)
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
        runThenCancel {
            intValue = watchableValueOf(4)
            val readOnly = intValue.readOnly()
            println("Object: $readOnly") // Coverage
            watch(readOnly) {
                changes += it
            }
            changes.expect(ValueChange(4, 4))
            intValue.set(5)
            assertEquals(5, readOnly.get())
            intValue.set(6)
            assertEquals(6, readOnly.get())
            changes.expect(ValueChange(4, 5))
            changes.expect(ValueChange(5, 6))
        }
    }

    @Test fun watchFromOtherScope() {
        runBlocking {
            intValue = scope.watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the other scope
            scope.coroutineContext.cancel()
            assertFalse(intValue.isActive)
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test fun watchOnOtherScope() {
        runThenCancel {
            intValue = watchableValueOf(5)
            scope.watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            delay(50)
            changes.expect(ValueChange(5, 17))

            // Shut down the other scope
            scope.coroutineContext.cancel()
            assertTrue(intValue.isActive) // Active, but we will not receive stuff
            intValue.set(88)
            changes.expectNone()
        }
    }

    companion object {
        private fun log(message: String) {
            println(Thread.currentThread().name + ": $message")
        }
    }
}
