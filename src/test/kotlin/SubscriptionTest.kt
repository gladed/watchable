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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Test

@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class SubscriptionTest {
    val changes = Channel<SetChange<Int>>(Channel.UNLIMITED)
    private val set = watchableSetOf(1)

    private fun <T> runThenCancel(func: suspend CoroutineScope.() -> T) = runBlocking {
        func().also { coroutineContext.cancelChildren() }
    }

    @Test fun `cancel immediately`() {
        runThenCancel {
            val handle = watch(set) { changes.send(it) }
            changes.expect(SetChange.Initial(setOf(1)))
            handle.cancel() // Instantly cancel, no more changes!
            set.use { add(2) }
            changes.expectNone()
        }
    }

    @Test fun `close drains all pending changes`() {
        runThenCancel {
            val handle = watch(set) { changes.send(it) }
            changes.expect(SetChange.Initial(setOf(1)))
            set.use { add(2) }
            handle.close()
            yield() // Allow other coroutines to process
            set.use { add(3) }
            changes.expect(SetChange.Add(listOf(2)))
            changes.expectNone()
        }
    }
}
