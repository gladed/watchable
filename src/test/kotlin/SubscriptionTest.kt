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

import io.gladed.watchable.SetChange
import io.gladed.watchable.batch
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

fun <T> runThenCancel(func: suspend CoroutineScope.() -> T) = runBlocking {
    func().also { coroutineContext.cancelChildren() }
}

@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SubscriptionTest {
    @Rule @JvmField val changes = ChangeWatcherRule<SetChange<Int>>()
    private val set = watchableSetOf(1)

    @Test fun `cancel immediately`() {
        runThenCancel {
            val handle = watch(set) { changes += it }
            changes.expect(SetChange.Initial(setOf(1)))
            handle.cancel() // Instantly cancel, no more changes!
            set.use { add(2) }
            changes.expectNone()
        }
    }

    @Test fun `close drains all pending changes`() {
        runThenCancel {
            val handle = watch(set) { changes += it }
            changes.expect(SetChange.Initial(setOf(1)))
            set.use { add(2) }
            log("Close watch handle")
            handle.close()
            log("Yield")
            yield() // Allow other coroutines to process
            log("Use")
            set.use { add(3) }
            log("Waiting for 2 to arrive")
            changes.expect(SetChange.Add(2))
            log("Making sure 3 never arrives")
            changes.expectNone()
        }
    }

    @Test fun `batch channel delivers immediately`() {
        runThenCancel {
            val tx = Channel<List<Int>>(5)
            tx.send(listOf(1))
            val rx = batch(tx, 1000)
            withTimeout(150) {
                assertEquals(listOf(1), rx.receive())
            }
        }
    }

    @Test fun `batch channel buffers outstanding content`() {
        runThenCancel {
            val tx = Channel<List<Int>>(5)
            tx.send(listOf(1))
            tx.send(listOf(2))
            val rx = batch(tx, 1000)
            assertEquals(listOf(1, 2), rx.receive())
        }
    }

    @Test fun `batch channel buffers later content`() {
        runThenCancel {
            val tx = Channel<List<Int>>(5)
            tx.send(listOf(1))
            tx.send(listOf(2))
            val rx = batch(tx, 150)
            assertEquals(listOf(1, 2), rx.receive())
            tx.send(listOf(3))
            tx.send(listOf(4))
            delay(100)
            assertEquals(null, rx.poll()) // Nothing there yet
            withTimeout(150) {
                assertEquals(listOf(3, 4), rx.receive())
            }
        }
    }

    @Test fun `ending content delivered on close`() {
        runBlocking {
            val tx = Channel<List<Int>>(5)
            tx.send(listOf(1))
            val rx = batch(tx, 1000)
            assertEquals(listOf(1), rx.receive())
            tx.send(listOf(2))
            tx.close()
            withTimeout(150) {
                assertEquals(listOf(2), rx.receive())
                assertTrue(rx.isClosedForReceive)
            }
        }
    }
}