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
import io.gladed.watchable.util.Cannot
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.cached
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.assertEquals
import org.junit.Test
import runTest
import java.util.UUID

@UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CacheTest {
    data class Thing(val id: String = UUID.randomUUID().toString(), val value: Int)

    private val store = mockk<Store<Thing>>(relaxUnitFun = true)

    interface CacheTestScope : TestCoroutineScope {
        val cache: Store<Thing>
    }

    private fun test(func: suspend CacheTestScope.() -> Unit) = runTest {
        (object : CacheTestScope, TestCoroutineScope by this {
            override val cache = store.cached(coroutineContext)
        }).func()
    }

    private val thing = Thing(value = 1)

    @Test fun `put puts`() = test {
        cache.put(thing.id, thing)
        coVerify { cache.put(eq(thing.id), eq(thing)) }
    }

    @Test fun `get gets`() = test {
        coEvery { store.get(thing.id) } returns thing
        assertEquals(thing, cache.get(thing.id))
    }

    @Test fun `second get gets from cache`() = test {
        coEvery { store.get(thing.id) } returns thing
        assertEquals(thing, cache.get(thing.id))
        assertEquals(thing, cache.get(thing.id))
        // Only one because 2nd gets from cache instead of store.
        coVerify(exactly = 1) { store.get(thing.id) }
    }

    @Test fun `put stores in cache`() = test {
        cache.put(thing.id, thing)
        assertEquals(thing, cache.get(thing.id))
        // Get comes from cache not backing store
        coVerify(exactly = 0) { store.get(thing.id) }
    }

    @Test fun `get slowly twice`() = test {
        val mutex = Mutex(locked = true)
        coEvery { store.get(thing.id) } coAnswers { mutex.withLock { thing } }
        val getOperations = listOf(async { cache.get(thing.id) }, async { cache.get(thing.id) })
        mutex.unlock()

        assertEquals(listOf(thing, thing), getOperations.awaitAll())
        // Only one call to prove we are caching attempts in the background
        coVerify(exactly = 1) { store.get(thing.id) }
    }

    @Test fun `put while get slowly`() = test {
        // Slowly return thing1
        val mutex = Mutex(locked = true)
        coEvery { store.get(thing.id) } coAnswers { mutex.withLock { thing } }
        val get1 = async { cache.get(thing.id) }

        // Store thing2 over thing1
        val thing2 = thing.copy(value = 2)
        cache.put(thing.id, thing2)

        // Allow thing1 get to complete
        mutex.unlock()
        assertEquals(thing, get1.await())

        // Make sure that new attempt gets thing2 (not the stale thing1)
        coEvery { store.get(thing.id) } returns thing2
        assertEquals(thing2, cache.get(thing.id))
    }

    @Test fun `delete while get slowly`() = test {
        // Slowly return thing
        val mutex = Mutex(locked = true)
        coEvery { store.get(thing.id) } coAnswers { mutex.withLock { thing } }
        val get1 = async { cache.get(thing.id) }

        //  Kill thing
        cache.remove(thing.id)

        // Allow thing1 get to complete
        mutex.unlock()
        assertEquals(thing, get1.await())

        coEvery { store.get(thing.id) } throws Cannot("get missing thing")
        impossible {
            cache.get(thing.id)
        }
    }

}
