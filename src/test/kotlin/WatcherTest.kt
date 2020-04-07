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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WatcherTest {
    val changes = Channel<Any>(Channel.UNLIMITED)

    @Test fun `receive events while flushing`() = runTest {
        val value = watchableValueOf(1)
        val handle = watch(value) { changes.send(it) }
        changes.mustBe(ValueChange(null, 1, true))
        value.set(2)
        handle.stop()
        changes.mustBe(ValueChange(1, 2))
    }

    @Test fun `get final batch`() = runTest {
        val changes = Channel<List<ValueChange<Int>>>(Channel.UNLIMITED)
        val value = watchableValueOf(1)
        val handle = batch(value, 500) { changes.send(it) }
        value.set(2)
        changes.mustBe()

        // close should cause an immediate flush of outstanding batch items regardless of its timeout.
        handle.stop()
        changes.mustBe(listOf(ValueChange(null, 1, true), ValueChange(1, 2)))
    }

    @Test fun `flush two`() = runTest {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        val handle = watch(list) { changes.send(it) } + watch(list) { changes.send(it) }
        handle.stop()
        changes.mustBe(ListChange.Initial(listOf(1)), ListChange.Initial(listOf(1)))
    }

    @Test fun `cancel two`() = runTest {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        pauseDispatcher {
            val handle = watch(list) { changes.send(it) } + watch(list) { changes.send(it) }
            handle.cancel()
        }
        changes.mustBe()
    }

    @Test fun `start twice`() = runTest {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        pauseDispatcher {
            val watcher = watch(list) { changes.send(it) }
            watcher.start()
            watcher.start()
            changes.mustBe(ListChange.Initial(listOf(1)))
        }
    }


    @Test fun `stop twice`() = runTest {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        val watcher = watch(list) { changes.send(it) }
        watcher.stop()
        watcher.stop()
        watcher.start()
        watcher.cancel()
        changes.mustBe(ListChange.Initial(listOf(1)))
    }

    @Test fun `cancel twice`() = runTest {
        val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)
        val list = watchableListOf(1)
        pauseDispatcher {
            val watcher = watch(list) { changes.send(it) }
            watcher.cancel()
            watcher.cancel()
            watcher.start()
            watcher.stop()
        }
        changes.mustBe()
    }

}
