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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.yield
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class CancelTest {
    private lateinit var intValue: WatchableValue<Int>
    val changes = Channel<ValueChange<Int>>(Channel.UNLIMITED)

    @Test fun `callbacks stop when scope joined`() = runTest {
        coroutineScope {
            println("In test: ${coroutineContext[Job]}")

            intValue = watchableValueOf(5)
            watch(intValue) { changes.send(it) }
            changes.mustBe(ValueChange(null, 5, true))
        }

        coroutineScope {
            // intValue can still be set
            intValue.set(88)
            Assert.assertEquals(88, intValue.value)
            // And it generates no changes
            changes.mustBe()
        }
    }

    @Test fun `callbacks stop when scope cancelled`() = runTest {
        intValue = watchableValueOf(5)
        val scope1 = CoroutineScope(coroutineContext + SupervisorJob())
        scope1.watch(intValue) { changes.send(it) }
        changes.mustBe(ValueChange(null, 5, true))
        intValue.set(17)
        changes.mustBe(ValueChange(5, 17))

        // Shut down the watching scope
        scope1.coroutineContext.cancel()
        intValue.set(88)
        changes.mustBe()
    }

    @Test fun `callbacks stop when job cancelled`() = runTest {
        intValue = watchableValueOf(5)
        val scope1 = CoroutineScope(coroutineContext)
        val job = scope1.watch(intValue) { changes.send(it) }

        changes.mustBe(ValueChange(null, 5, true))
        intValue.set(17)
        changes.mustBe(ValueChange(5, 17))

        // Shut down the job
        job.cancel()
        intValue.set(88)
        changes.mustBe()
    }

    @Test fun `watch allows parent scope to join`() = runTest {
        intValue = watchableValueOf(5)
        watch(intValue) { changes.send(it) }
        changes.mustBe(ValueChange(null, 5, true))
    }

    @Test fun `throw during watch destroys everything`() {
        try {
            runTest {
                intValue = watchableValueOf(5)
                watch(intValue) {
                    changes.send(it)
                    throw IllegalStateException("Whoops!")
                }
            }
            fail("must not reach")
        } catch (e: IllegalStateException) { }
    }
}
