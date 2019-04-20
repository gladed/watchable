import external.Adapter
import model.Bird
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
        birds.put(robin.id, with(Bird) { robin.inflate() })
    }

    @Test fun `read and write between adapter instances`() {
        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            birds.put(robin.id, with(Bird) { robin.inflate() })
        }

        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            assertEquals(robin, with(Bird) { birds.get(robin.id).deflate() })
        }
    }

    @Test(timeout = 500) fun `after create, modified changes are committed`() {
        runTest {
            val adapter = Adapter(coroutineContext, folder.root)
            inScope {
                val birds = adapter.birds.create(this)

                val liveRobin = with(Bird) { robin.inflate() }
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