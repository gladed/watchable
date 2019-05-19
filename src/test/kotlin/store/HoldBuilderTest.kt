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

import io.gladed.watchable.store.Hold
import io.gladed.watchable.store.HoldBuilder
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class HoldBuilderTest {
    private val hold = mockk<Hold>(relaxUnitFun = true)

    @Test fun `hold builder onStart works`() = runBlockingTest {
        HoldBuilder().apply {
            onStart { hold.onStart() }
            onStart { hold.onStart() }
        }.build().onStart()
        coVerify(exactly = 2) { hold.onStart() }
    }

    @Test fun `hold builder onCreate works`() = runBlockingTest {
        HoldBuilder().apply {
            onCreate { hold.onCreate() }
            onCreate { hold.onCreate() }
        }.build().onCreate()
        coVerify(exactly = 2) { hold.onCreate() }
    }

    @Test fun `hold builder onCancel works`() = runBlockingTest {
        HoldBuilder().apply {
            onCancel { hold.onCancel() }
            onCancel { hold.onCancel() }
        }.build().onCancel()
        verify(exactly = 2) { hold.onCancel() }
    }

    @Test fun `hold builder onStop works`() = runBlockingTest {
        HoldBuilder().apply {
            onStop { hold.onStop() }
            onStop { hold.onStop() }
        }.build().onStop()
        coVerify(exactly = 2) { hold.onStop() }
    }

    @Test fun `hold builder onRemove works`() = runBlockingTest {
        HoldBuilder().apply {
            onRemove { hold.onRemove() }
            onRemove { hold.onRemove() }
        }.build().onRemove()
        coVerify(exactly = 2) { hold.onRemove() }
    }
}
