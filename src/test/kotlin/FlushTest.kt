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

import io.gladed.watchable.ListChange
import io.gladed.watchable.ValueChange
import io.gladed.watchable.batch
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FlushTest : ScopeTest() {

    @Test(timeout = 500)
    fun `receive events while flushing`() {
        val changes = Channel<ValueChange<Int>>(Channel.UNLIMITED)
        val value = watchableValueOf(1)
        val handle = watch(value) { changes.send(it) }
        runBlocking {
            changes.expect(ValueChange(1))
            value.assign(2)
            handle.close()
            changes.expect(ValueChange(2))
            handle.join()
        }
    }

    @Test(timeout = 500)
    fun `get final batch`() {
        runBlocking {
            val changes = Channel<List<ValueChange<Int>>>(Channel.UNLIMITED)
            val value = watchableValueOf(1)
            val handle = batch(value, 1000) { changes.send(it) }
            changes.expect(listOf((ValueChange(1))))
            value.assign(2)
            // close should cause an immediate flush of outstanding batch items regardless of its timeout.
            changes.expectNone()
            handle.close()
            changes.expect(listOf(ValueChange(2)), timeout = 100)
            handle.join()
        }
    }

    @Test(timeout = 500) fun `flush two`() = runBlocking {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        val handle = watch(list) { changes.send(it) } + watch(list) { changes.send(it) }
        handle.closeAndJoin()
        changes.expect(ListChange.Add(0, listOf(1)), ListChange.Add(1, listOf(1)))
    }

    @Test(timeout = 500) fun `cancel two`() = runBlocking {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        val handle = watch(list) { changes.send(it) } + watch(list) { changes.send(it) }
        handle.cancel()
        changes.expectNone()
    }
}
