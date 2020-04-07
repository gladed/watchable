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

import io.gladed.watchable.Period
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.waitFor
import io.gladed.watchable.watch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import org.junit.Assert
import org.junit.Test

class OperationsTest {
    val changes = Channel<Int>(Channel.UNLIMITED)

    @Test fun `wait for something`() = runTest {
        val watchable = 5.toWatchableValue()
        val result = async { waitFor(watchable) { watchable.value == 6 } }
        Assert.assertFalse(result.isCompleted)
        watchable { value = 6 }
        Assert.assertTrue(result.isCompleted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `gentle stop of watch`() = runTest {
        val watchable = 5.toWatchableValue()
        val watcher = watch(watchable) { changes.send(it.value) }
        pauseDispatcher {
            watchable { value = 6 }
            watcher.stop()
            watchable { value = 7 }
        }
        changes.mustBe(5, 6)
        changes.mustBe()
    }

    @Test fun coverage() {
        cover(Period)
    }
}
