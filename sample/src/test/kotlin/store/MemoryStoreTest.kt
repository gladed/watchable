package store

import util.impossible
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Test
import util.runTest

@UseExperimental(FlowPreview::class)
class MemoryStoreTest {
    private val store = MemoryStore<Thing>("thing")
    val thing = Thing(value = 1)

    @Test fun `put and get`() = runTest {
        store.put(thing.id, thing)
        assertEquals(thing, store.get(thing.id))
    }

    @Test fun `get all keys`() = runTest {
        store.put(thing.id, thing)
        assertEquals(listOf(thing.id), store.keys().toList())
    }

    @Test fun `remove by id`() = runTest {
        store.put(thing.id, thing)
        store.delete(thing.id)
        impossible {
            store.get(thing.id)
        }
    }
}
