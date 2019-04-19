import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.coroutineScope
import model.Bird
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import store.HoldingStore
import store.HoldingStoreCallbacks
import store.MemoryStore
import store.ScopedStore


class StoreTest {
    private val robin = Bird(name = "robin")

    private val memory = MemoryStore<Bird>()

    val cbs = mockk<HoldingStoreCallbacks<Bird, String>>(relaxUnitFun = true)
    private val holder = object : HoldingStore<Bird, String>(memory),
        HoldingStoreCallbacks<Bird, String> by cbs { }


    @Before
    fun setup() {
        coEvery { cbs.onHold(any()) } answers { (args[0] as Bird).id }
    }

    @Test fun `memory store gets and puts`() = runTest {
        memory.put(robin.id, robin)
        assertEquals(robin, memory.get(robin.id))
    }

    @Test fun `hold something`() = runTest {
        memory.put(robin.id, robin)
        holder.get(robin.id)
        coVerify { cbs.onHold(robin) }
    }

    @Test fun `release something`() = runTest {
        memory.put(robin.id, robin)
        holder.get(robin.id).release()
        coVerify { cbs.onHold(robin) }
        coVerify { cbs.onRelease(robin, robin.id) }
    }

    @Test fun `release when scope completes`() = runTest {
        val scopeStore = ScopedStore(coroutineContext, holder)
        memory.put(robin.id, robin)

        inScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
            coVerify { cbs.onHold(robin) }
        }
        coVerify { cbs.onRelease(robin, robin.id) }
    }

    @Test fun `hold only once when two scopes get`() = runTest {
        val scopeStore = ScopedStore(coroutineContext, holder)
        memory.put(robin.id, robin)

        inScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
            coVerify { cbs.onHold(robin) }
            inScope {
                assertEquals(robin, scopeStore.create(this).get(robin.id))
                coVerify(exactly = 1) { cbs.onHold(robin) }
            }
        }
    }

    @Test fun `release only when both scopes close`() = runTest {
        val scopeStore = ScopedStore(coroutineContext, holder)
        memory.put(robin.id, robin)

        inScope {
            scopeStore.create(this).get(robin.id)
            inScope {
                scopeStore.create(this).get(robin.id)
            }
            coVerify(exactly = 0) { cbs.onRelease(robin, robin.id) }
        }
        coVerify(exactly = 1) { cbs.onRelease(robin, robin.id) }
    }

    @Test fun `hold+release only once when get twice from same scope`() = runTest {
        val scopeStore = ScopedStore(coroutineContext, holder)
        memory.put(robin.id, robin)

        inScope {
            val store = scopeStore.create(this)
            store.get(robin.id)
            store.get(robin.id)
            coVerify(exactly = 1) { cbs.onHold(robin) }
        }
        coVerify(exactly = 1) { cbs.onRelease(robin, robin.id) }
    }

    @Test fun `second user must reallocate`() = runTest {
        val scopeStore = ScopedStore(coroutineContext, holder)
        memory.put(robin.id, robin)

        inScope { scopeStore.create(this).get(robin.id) }
        inScope { scopeStore.create(this).get(robin.id) }

        coVerify(exactly = 2) { cbs.onHold(robin) }
        coVerify(exactly = 2) { cbs.onRelease(robin, robin.id) }
    }

    // TODO: Cancel an attempt to get that is in progress
    // TODO: Test piggybacking of get attempts
}
