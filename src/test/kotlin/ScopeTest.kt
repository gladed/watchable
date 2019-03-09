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
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class ScopeTest {
    private lateinit var intValue: WatchableValue<Int>
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "scope1")
    }.asCoroutineDispatcher()

    private val scope1 = LocalScope(dispatcher)

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
            Assert.assertEquals(88, intValue.get())
            // But, it's not active
            Assert.assertFalse(intValue.isActive)
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
                // Can't start watching; it's dead.
                watch(intValue) { }
            }
        } catch (e: IllegalStateException) { }
    }


    @Test
    fun watchFromOtherScope() {
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
            Assert.assertFalse(intValue.isActive)
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test
    fun watchOnOtherScope() {
        runBlocking {
            intValue = watchableValueOf(5)
            scope1.watch(intValue) {
                log("Handling $it")
                Assert.assertThat(Thread.currentThread().name, CoreMatchers.containsString("scope1"))
                // Because we're watching from this scope it should be named here
                changes += it
            }

            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the other scope
            scope1.coroutineContext.cancel()
            Assert.assertTrue(intValue.isActive) // Active, but we will not receive stuff
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test
    fun cancelJob() {
        runBlocking {
            intValue = watchableValueOf(5)
            val job = scope1.watch(intValue) {
                log("Handling $it")
                Assert.assertThat(Thread.currentThread().name, CoreMatchers.containsString("scope1"))
                // Because we're watching from this scope it should be named here
                changes += it
            }

            changes.expect(ValueChange(5, 5))
            intValue.set(17)
            changes.expect(ValueChange(5, 17))

            // Shut down the job
            job.cancel()
            Assert.assertTrue(intValue.isActive) // Active, but we will not receive stuff
            intValue.set(88)
            changes.expectNone()
        }
    }

    @Test
    fun noBlock() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) {
                changes += it
            }
            changes.expect(ValueChange(5, 5))
        }
    }

    @Test
    fun throwDuringWatch() {
        runBlocking {
            intValue = watchableValueOf(5)
            val watchJob = watch(intValue) {
                changes += it
                throw IllegalStateException("Whoops!")
            }
            changes.expect(ValueChange(5, 5))
            intValue.set(7)
            changes.expectNone()
            Assert.assertTrue(watchJob.isCancelled)
        }
    }

}
