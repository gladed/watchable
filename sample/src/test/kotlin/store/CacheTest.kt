package store

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Assert.assertEquals
import org.junit.Test
import util.TestCoroutineScope
import util.runTest

@UseExperimental(FlowPreview::class)
class CacheTest {
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
        trigger()
        mutex.unlock()

        assertEquals(listOf(thing, thing), getOperations.awaitAll())
        // Only one call to prove we are caching attempts in the background
        coVerify(exactly = 1) { store.get(thing.id) }
    }
}
