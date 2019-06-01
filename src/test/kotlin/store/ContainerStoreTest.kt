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

import io.gladed.watchable.Change
import io.gladed.watchable.Watchable
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.store.Cannot
import io.gladed.watchable.store.Container
import io.gladed.watchable.store.Hold
import io.gladed.watchable.store.HoldingStore
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.create
import io.gladed.watchable.store.holding
import io.gladed.watchable.toWatchableValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.util.UUID

@UseExperimental(ExperimentalCoroutinesApi::class)
class ContainerStoreTest {

    data class Bird(
        val id: String = UUID.randomUUID().toString(),
        val name: WatchableValue<String>) : Container {
        override val watchables: Watchable<Change> = name
    }

    private val hold = mockk<Hold>(relaxUnitFun = true)

    private val rootStore = mockk<Store<Bird>>(relaxUnitFun = true)
    interface TestScope : TestCoroutineScope {
        val scopeStore: HoldingStore<Bird>
    }

    private fun test(func: suspend TestScope.() -> Unit) = runBlockingTest {
        (object : TestScope, TestCoroutineScope by this {
            override val scopeStore = holding(rootStore) {
                onCancel { hold.onCancel() }
                onCreate { hold.onCreate() }
                onRemove { hold.onRemove() }
                onStop { hold.onStop() }
                onStart { hold.onStart() }
            }
        }).func()
    }


    interface ContainerWatchCreator {
        suspend fun create(bird: Bird): Hold
    }

    private val bird = Bird(name = "one".toWatchableValue())
    private val backingStore = mockk<Store<Bird>>(relaxUnitFun = true)
    private val holdCreator = mockk<ContainerWatchCreator>()

    @Test fun `push contained changes`() = test {
        coEvery { backingStore.get(bird.id) } throws Cannot("do that")
        coEvery { holdCreator.create(bird) } returns hold
        val holding = holding(backingStore) { holdCreator.create(it) }

        coroutineScope {
            val scopedStore = holding.create(this)
            scopedStore.put(bird.id, bird)
            coVerify(exactly = 1) { backingStore.put(bird.id, bird) }

            // Now change the internals of the bird which will eventually trigger re-save
            bird.name.set("two")
        }
        // Make sure the second call happens (scope close = batch close)
        coVerify(exactly = 2) { backingStore.put(bird.id, bird) }
    }

    @Test fun `store only once when container added`() = test {
        coEvery { backingStore.get(bird.id) } throws Cannot("do that")
        coEvery { holdCreator.create(bird) } returns hold

        val holding = holding(backingStore) { holdCreator.create(it) }
        coroutineScope {
            val scoped = holding.create(this)
            scoped.put(bird.id, bird)
        }
        coVerify(exactly = 1) { backingStore.put(bird.id, bird) }
    }

    @Test fun `do not push changes on deleted container item`() = test {
        coEvery { backingStore.get(bird.id) } throws Cannot("do that")
        coEvery { holdCreator.create(bird) } returns hold
        val holding = holding(backingStore) { holdCreator.create(it) }

        coroutineScope {
            val scopedStore = create(holding)
            scopedStore.put(bird.id, bird)
            // Change bird internals then remove it
            bird.name.set("two")
            scopedStore.remove(bird.id)
            coVerify(exactly = 1) { backingStore.remove(bird.id) }
        }
        // Put only once because the bird was later removed
        coVerify(exactly = 1) { backingStore.put(bird.id, bird) }
    }

    @Test fun `manual stop hold`() = test {
        coEvery { backingStore.get(bird.id) } throws Cannot("do that")
        coEvery { holdCreator.create(bird) } returns hold
        val holdingStore = holding(backingStore) { holdCreator.create(it) }
        coroutineScope {
            val scoped = create(holdingStore)
            scoped.put(bird.id, bird) // Creates a hold as well as the object
            holdingStore.stop(this, bird.id) // Stops holding
            bird.name.set("two")
        }
        // Put only once because the hold was stopped
        coVerify(exactly = 1) { backingStore.put(bird.id, bird) }
    }


    @Test fun `revify hold after stopping it`() = test {
        coEvery { backingStore.get(bird.id) } throws Cannot("do that")
        coEvery { holdCreator.create(bird) } returns hold
        val holdingStore = backingStore.holding(coroutineContext) { holdCreator.create(it) }
        coroutineScope {
            val scoped = create(holdingStore)
            scoped.put(bird.id, bird) // Creates a hold as well as the object
            holdingStore.stop(this, bird.id) // Stops holding

            coEvery { backingStore.get(bird.id) } returns bird
            scoped.get(bird.id) // Re-creates hold
            bird.name.set("two")
        }
        // Put TWICE because the hold was restored
        coVerify(exactly = 2) { backingStore.put(bird.id, bird) }
    }
}
