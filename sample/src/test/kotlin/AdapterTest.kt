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

    @Test(timeout = 500) fun `after create, modified changes are committed`() {
        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            inScope {
                val birds = adapter.birds.create(this)

                val liveRobin = MutableBird.inflate(robin)
                birds.put(robin.id, liveRobin)
                liveRobin.following { add("123") }
            }
        }

        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            assertEquals(listOf("123"), birds.get(robin.id).following)
        }
    }
}