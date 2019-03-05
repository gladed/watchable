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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

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
            assertTrue(intValue.isActive)
            changes.expect(ValueChange(5, 17))
            log("At end, shutting down")
        }
    }

    @Test fun noCallbackAfterScopeClose() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
        }

        runBlocking {
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
        runBlocking {
            intValue = watchableValueOf(5)
        }

        try {
            runBlocking {
                watch(intValue) { }
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun setSameValue() {
        runBlocking {
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
            assertEquals(5, readOnly.get())
            intValue.set(6)
            assertEquals(6, readOnly.get())
            changes.expect(ValueChange(4, 5))
            changes.expect(ValueChange(5, 6))
            assertEquals(6, readOnly.value)
        }
    }

    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "scope1")
    }.asCoroutineDispatcher()

    private val scope1 = LocalScope(dispatcher)

    @Test fun watchFromOtherScope() {
        runBlocking {
            intValue = scope1.watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the other scope
            scope1.coroutineContext.cancel()
            assertFalse(intValue.isActive)
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test fun watchOnOtherScope() {
        runBlocking {
            intValue = watchableValueOf(5)
            scope1.watch(intValue) {
                log("Handling $it")
                assertThat(Thread.currentThread().name, containsString("scope1"))
                // Because we're watching from this scope it should be named here
                changes += it
            }

            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the other scope
            scope1.coroutineContext.cancel()
            assertTrue(intValue.isActive) // Active, but we will not receive stuff
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test fun cancelJob() {
        runBlocking {
            intValue = watchableValueOf(5)
            val job = scope1.watch(intValue) {
                log("Handling $it")
                assertThat(Thread.currentThread().name, containsString("scope1"))
                // Because we're watching from this scope it should be named here
                changes += it
            }

            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the job
            job.cancel()
            assertTrue(intValue.isActive) // Active, but we will not receive stuff
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test fun noBlock() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
        }
    }

    @Test fun throwDuringWatch() {
        runBlocking {
            intValue = watchableValueOf(5)
            val watchJob = watch(intValue) {
                changes += it
                throw IllegalStateException("Whoops!")
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(7)
            changes.expectNone()
            assertTrue(watchJob.isCancelled)
        }
    }

    companion object {
        private fun log(message: String) {
            println(Thread.currentThread().name + ": $message")
        }
    }
}
