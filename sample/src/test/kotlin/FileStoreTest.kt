import external.FileStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FileStoreTest {
    @Rule @JvmField val folder = TemporaryFolder()
    private lateinit var fileStore: FileStore

    @Before fun setup() {
        fileStore = FileStore(folder.root)
    }

    @Test fun `create bird`() {
        runBlocking {
            with(fileStore) {
                val bird = makeBird("robin")
                assertEquals("robin", bird.name.get())
            }
        }
    }

    @Test fun `get same bird back from cache`() {
        runBlocking {
            with(fileStore) {
                val bird = makeBird("robin")
                // We can get a reference to the very same bird.
                assertTrue(bird === findBird(bird.id))
            }
        }
    }

    @Test fun `get same bird back from disk`() {
        val oldBird = runBlocking {
            println("Context: $coroutineContext")
            fileStore.makeBird("robin")
        }

        runBlocking {
            val newBird = fileStore.findBird(oldBird.id)
            assertTrue(newBird !== oldBird) // NOT the same bird, a new copy
            assertEquals("robin", newBird?.name?.get())
        }
    }

    @Test fun `change bird content`() {
        val (robinId, wrenId) = runBlocking {
            with(fileStore) {
                val robin = makeBird("robin")
                val wren = makeBird("wren")
                robin.following.use {
                    add(wren.id)
                }
                robin.id to wren.id
            }
        }

        // From disk...
        runBlocking {
            val robin = fileStore.findBird(robinId)!!
            assertEquals(listOf(wrenId), robin.following.get())
        }
    }
}