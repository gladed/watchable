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
import io.gladed.watchable.store.plus
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import runTest

class HoldTest {
    private val watcher = mockk<Watcher>(relaxUnitFun = true)
    private val mockHold = mockk<Hold>(relaxUnitFun = true)
    private val hold = Hold(onCancel = { mockHold.onCancel() },
        onRemove = { mockHold.onRemove() },
        onStart = { mockHold.onStart() },
        onStop = { mockHold.onStop() },
        onCreate = { mockHold.onCreate() })

    @Test fun `plus combines cancel`() = runTest {
        (hold + hold).onCancel()
        verify(exactly = 2) { mockHold.onCancel() }
    }

    @Test fun `plus combines remove`() = runTest {
        (hold + hold).onRemove()
        coVerify(exactly = 2) { mockHold.onRemove() }
    }

    @Test fun `plus combines start`() = runTest {
        (hold + hold).onStart()
        coVerify(exactly = 2) { mockHold.onStart() }
    }

    @Test fun `plus combines stop`() = runTest {
        (hold + hold).onStop()
        coVerify(exactly = 2) { mockHold.onStop() }
    }

    @Test fun `plus combines create`() = runTest {
        (hold + hold).onCreate()
        coVerify(exactly = 2) { mockHold.onCreate() }
    }


    @Test fun `plus watcher combines cancel`() = runTest {
        (hold + watcher).onCancel()
        verify(exactly = 1) { mockHold.onCancel() }
        verify(exactly = 1) { watcher.cancel() }
    }

    @Test fun `plus watcher allows remove`() = runTest {
        (hold + watcher).onRemove()
        coVerify(exactly = 1) { mockHold.onRemove() }
        // Watcher has no remove
    }

    @Test fun `plus watcher combines start`() = runTest {
        (hold + watcher).onStart()
        coVerify(exactly = 1) { mockHold.onStart() }
        coVerify(exactly = 1) { watcher.start() }
    }

    @Test fun `plus watcher combines stop`() = runTest {
        (hold + watcher).onStop()
        coVerify(exactly = 1) { mockHold.onStop() }
        coVerify(exactly = 1) { watcher.stop() }
    }
}
