import external.Adapter
import model.Bird
import model.MutableBird
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AdapterTest {
    @Rule @JvmField val folder = TemporaryFolder()
    private val robin = Bird(name = "robin")

    @Test fun `read and write`() = runTest {
        val adapter = Adapter(coroutineContext, folder.root)
        val birds = adapter.birds.create(this)
        birds.put(robin.id, MutableBird.inflate(robin))
    }

    @Test fun `read and write between adapter instances`() {
        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            birds.put(robin.id, MutableBird.inflate(robin))
        }

        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            assertEquals(robin, MutableBird.deflate(birds.get(robin.id)))
        }
    }
}