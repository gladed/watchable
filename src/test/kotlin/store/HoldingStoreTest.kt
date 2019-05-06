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

import impossible
import io.gladed.watchable.store.Cannot
import io.gladed.watchable.store.Hold
import io.gladed.watchable.store.HoldingStore
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.holding
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import runTest
import java.util.UUID

@UseExperimental(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class HoldingStoreTest {
    data class Bird(val id: String = UUID.randomUUID().toString(), val name: String)

    private val robin = Bird(name = "robin")

    private val rootStore = mockk<Store<Bird>>(relaxUnitFun = true)

    interface WatchCreator {
        suspend fun create(bird: Bird): Hold
    }

    private val creator = mockk<WatchCreator>()

    private val hold = mockk<Hold>(relaxUnitFun = true)

    interface HoldingStoreScope : TestCoroutineScope {
        val scopeStore: HoldingStore<Bird>
    }

    private fun test(func: suspend HoldingStoreScope.() -> Unit) = runTest {
        (object : HoldingStoreScope, TestCoroutineScope by this {
            override val scopeStore = rootStore.holding(coroutineContext) { creator.create(it) }
        }).func()
    }

    @Before
    fun setup() {
        coEvery { creator.create(robin) } returns hold
        coEvery { rootStore.get(robin.id) } returns robin
    }

    @Test fun `put an item`() = test {
        coEvery { rootStore.get(robin.id) } throws Cannot("find bird for key")
        scopeStore.create(this).put(robin.id, robin)
        coVerify { creator.create(robin) }
        coVerify { rootStore.put(robin.id, robin) }
    }

    @Test fun `release when scope completes`() = test {
        coroutineScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
            coVerify { creator.create(robin) }
        }
        coVerify { hold.onStop() }
    }

    @Test fun `hold only once when two scopes get`() = test {
        assertEquals(robin, scopeStore.create(this).get(robin.id))
        coVerify(exactly = 1) { creator.create(robin) }
        coroutineScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
            coVerify(exactly = 1) { creator.create(robin) }
        }
    }

    @Test fun `release only when both scopes close`() = test {
        coroutineScope {
            scopeStore.create(this).get(robin.id)
            coroutineScope {
                scopeStore.create(this).get(robin.id)
            }
            coVerify(exactly = 0) { hold.onStop() }
        }
        coVerify(exactly = 1) { hold.onStop() }
    }

    @Test fun `hold+release only once when get twice from same scope`() = test {
        coroutineScope {
            val store = scopeStore.create(this)
            store.get(robin.id)
            store.get(robin.id)
            coVerify(exactly = 1) { creator.create(robin) }
        }
        coVerify(exactly = 1) { hold.onStop() }
    }

    @Test fun `second user must reallocate`() = test {
        coroutineScope { scopeStore.create(this).get(robin.id) }
        coroutineScope { scopeStore.create(this).get(robin.id) }

        coVerify(exactly = 2) { creator.create(robin) }
        coVerify(exactly = 2) { hold.onStop() }
    }

    @Test fun `failure to get causes throw`() = test {
        coEvery { rootStore.get(eq(robin.id)) } throws Cannot("find value for that key")

        coroutineScope {
            val store = scopeStore.create(this)
            try {
                store.get(robin.id)
                fail("Should have failed")
            } catch (c: Cannot) {
                println("Threw $c (as expected)")
            }
        }
    }

    @Test fun `second attempt succeeds`() = test {
        coEvery { rootStore.get(robin.id) } throws Cannot("find value for that key")

        coroutineScope {
            val store = scopeStore.create(this)
            try {
                store.get(robin.id)
            } catch (c: Exception) { }

            coEvery { rootStore.get(robin.id) } returns robin
            assertEquals(robin, store.get(robin.id))
        }
    }

    @Test fun `two attempts collude`() = test {
        val scope1 = CoroutineScope(coroutineContext + SupervisorJob())
        val scope2 = CoroutineScope(coroutineContext + SupervisorJob())

        val birds = listOf(async {
            scopeStore.create(scope1).get(robin.id)
        }, async {
            scopeStore.create(scope2).get(robin.id)
        }).awaitAll()
        assertEquals(listOf(robin, robin), birds)

        // Should only be one hold and one release even though two scopes were getting
        coVerify(exactly = 1) { creator.create(robin) }

        scope1.cancel()
        coVerify(exactly = 0) { hold.onStop() }
        scope2.cancel()
        coVerify(exactly = 1) { hold.onStop() }
    }

    @Test fun `attempt to get is cancelled when scope dies`() = test {
        val mutex = Mutex(locked = true)
        coEvery { rootStore.get(robin.id) } coAnswers {
            mutex.withLock { robin }
        }
        val store = scopeStore.create(this)
        val deferred = async {
            store.get(robin.id)
        }
        deferred.cancel()

        // Return to normal function
        coEvery { rootStore.get(eq(robin.id)) } returns robin
        assertEquals(robin, store.get(robin.id))
    }

    @Test fun `delete stops hold`() = test {
        coroutineScope {
            val store = scopeStore.create(this)
            store.get(robin.id)
            store.remove(robin.id)
            coVerify { hold.onStop() }
            coVerify { rootStore.remove(robin.id) }
        }
    }

    @Test fun `cannot re-put`() = test {
        coroutineScope {
            val store = scopeStore.create(this)
            store.get(robin.id)
            impossible {
                store.put(robin.id, robin.copy())
            }
        }
    }

    @Test fun `stop everybody`() = test {
        scopeStore.create(this).get(robin.id)
        scopeStore.stop()
        // Released even though scope still active
        coVerify { hold.onStop() }
    }

    @UseExperimental(FlowPreview::class)
    @Test fun `keys goes to back`() = test {
        coEvery { rootStore.keys() } returns listOf(robin.id).asFlow()
        assertEquals(listOf(robin.id), scopeStore.create(this).keys().toList())
    }
}
