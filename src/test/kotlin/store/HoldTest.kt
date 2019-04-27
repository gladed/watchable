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

package store

import io.gladed.watchable.Watcher
import io.gladed.watchable.store.Hold
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import runTest

class HoldTest {
    private val watcher = mockk<Watcher>(relaxUnitFun = true)
    private val mockHold = mockk<Hold>(relaxUnitFun = true)
    private val hold = Hold(onCancel = { mockHold.cancel() },
        onRemove = { mockHold.remove() },
        onStart = { mockHold.start() },
        onStop = { mockHold.stop() })

    @Test fun `plus combines cancel`() = runTest {
        (hold + hold).cancel()
        verify(exactly = 2) { mockHold.cancel() }
    }

    @Test fun `plus combines remove`() = runTest {
        (hold + hold).remove()
        coVerify(exactly = 2) { mockHold.remove() }
    }

    @Test fun `plus combines start`() = runTest {
        (hold + hold).start()
        coVerify(exactly = 2) { mockHold.start() }
    }

    @Test fun `plus combines stop`() = runTest {
        (hold + hold).stop()
        coVerify(exactly = 2) { mockHold.stop() }
    }

    @Test fun `plus watcher combines cancel`() = runTest {
        (hold + watcher).cancel()
        verify(exactly = 1) { mockHold.cancel() }
        verify(exactly = 1) { watcher.cancel() }
    }

    @Test fun `plus watcher allows remove`() = runTest {
        (hold + watcher).remove()
        coVerify(exactly = 1) { mockHold.remove() }
        // Watcher has no remove
    }

    @Test fun `plus watcher combines start`() = runTest {
        (hold + watcher).start()
        coVerify(exactly = 1) { mockHold.start() }
        coVerify(exactly = 1) { watcher.start() }
    }

    @Test fun `plus watcher combines stop`() = runTest {
        (hold + watcher).stop()
        coVerify(exactly = 1) { mockHold.stop() }
        coVerify(exactly = 1) { watcher.stop() }
    }
}
