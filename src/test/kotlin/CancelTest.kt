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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.Executors

class CancelTest {
    private lateinit var intValue: WatchableValue<Int>
    val changes = Channel<ValueChange<Int>>(Channel.UNLIMITED)

    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "scope1")
    }.asCoroutineDispatcher()

    private val scope1 = LocalScope(dispatcher)

    @Test fun `callbacks stop when scope joined`() {
        runBlocking {
            intValue = watchableValueOf(5)
            watch(intValue) { changes.send(it) }
            changes.expect(ValueChange(5))
        }

        runBlocking {
            // intValue can still be set
            intValue.assign(88)
            Assert.assertEquals(88, intValue.value)
            // And it generates no changes
            changes.expectNone()
        }
    }

    @Test fun `callbacks stop when scope cancelled`() = runBlocking {
        intValue = watchableValueOf(5)
        scope1.watch(intValue) {
            Assert.assertThat(Thread.currentThread().name, CoreMatchers.containsString("scope1"))
            changes.send(it)
        }
        changes.expect(ValueChange(5))
        intValue.assign(17)
        changes.expect(ValueChange(17))

        // Shut down the watching scope
        scope1.coroutineContext.cancel()
        intValue.assign(88)
        changes.expectNone()
    }

    @Test
    fun `callbacks stop when job cancelled`() = runBlocking {
        intValue = watchableValueOf(5)
        val job = scope1.watch(intValue) {
            Assert.assertThat(Thread.currentThread().name, CoreMatchers.containsString("scope1"))
            // Because we're watching from this scope it should be named here
            changes.send(it)
        }

        changes.expect(ValueChange(5))
        intValue.assign(17)
        changes.expect(ValueChange(17))

        // Shut down the job
        job.cancel()
        intValue.assign(88)
        changes.expectNone()
    }

    @Test
    fun `watch allows parent scope to join`() = runBlocking {
        intValue = watchableValueOf(5)
        watch(intValue) { changes.send(it) }
        changes.expect(ValueChange(5))
    }

    @Test
    fun `throw during watch cancels job`() = runBlocking {
        intValue = watchableValueOf(5)
        val handle = watch(intValue) {
            changes.send(it)
            throw IllegalStateException("Whoops!")
        }
        changes.expect(ValueChange(5))
        intValue.assign(7)
        changes.expectNone()
        handle.cancel()
    }
}
