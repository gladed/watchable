import external.FileStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FileStoreTest {
    @Rule @JvmField val folder = TemporaryFolder()

    private fun <T> withFileStore(func: suspend FileStore.() -> T) =
        runBlocking {
            // Sadly there is no "flush" to synchronize with all outstanding changes. So we will delay.
            val fileStore = FileStore(coroutineContext, folder.root, 25)
            fileStore.func().also { delay(50) }
        }

    @Test fun `create bird`() {
        withFileStore {
            val bird = makeBird("robin")
            assertEquals("robin", bird.name.value)
        }
    }

    @Test fun `get same bird back from cache`() {
        withFileStore {
            val bird = makeBird("robin")
            // We can get a reference to the very same bird.
            assertTrue(bird === findBird(bird.id))
        }
    }

    @Test fun `get same bird back from disk`() {
        val oldBird = withFileStore {
            println("Context: $coroutineContext")
            makeBird("robin")
        }

        withFileStore {
            val newBird = findBird(oldBird.id)
            assertTrue(newBird !== oldBird) // NOT the same bird, a new copy
            assertEquals("robin", newBird?.name?.value)
        }
    }

    @Test fun `change bird content`() {
        val (robinId, wrenId) = withFileStore {
            val robin = makeBird("robin")
            val wren = makeBird("wren")
            robin.following.use {
                add(wren.id)
            }
            robin.id to wren.id
        }

        // New file store instance must load from disk
        withFileStore {
            val robin = findBird(robinId)!!
            assertEquals(listOf(wrenId), robin.following)
        }
    }
}
