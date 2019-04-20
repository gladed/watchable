import logic.Logic
import model.Bird
import model.MutableBird
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import store.Cannot
import store.MemoryStore

class LogicTest {
    private val robin = Bird(name = "robin")
    private val wren = Bird(name = "wren")
    private val birdStore = MemoryStore<Bird>()

    /** Run a test with a `logic` object backed by RAM. */
    private fun test(func: suspend Context.() -> Unit) {
        runTest {
            (object : Context, TestCoroutineScope by this {
                override val logic = Logic(coroutineContext, birdStore)
            }).func()
        }
    }

    @Test fun `write bird to store`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            logic.birds.create(this).put(robin.id, bird)
            assertEquals(robin, birdStore.get(robin.id))
        }
    }

    @Test fun `update bird in store`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            logic.birds.create(this).put(robin.id, bird)
            bird.name.set("robin2")
            trigger() // Let stuff happen
            assertEquals("robin2", birdStore.get(robin.id).name)
        }
    }

    @Test fun `cannot update deleted bird`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            val birds = logic.birds.create(this)
            birds.put(robin.id, bird)
            birds.delete(robin.id)
            // We can change it but it makes no difference
            bird.name.set("robin2")

            trigger() // Let stuff happen

            impossible {
                birdStore.get(robin.id)
            }
        }
    }

    @Test fun `cannot follow bird that does not exist`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            logic.birds.create(this).put(robin.id, bird)

            impossible {
                bird.following.add(wren.id)
            }
        }
    }

    companion object {
        private interface Context : TestCoroutineScope {
            val logic: Logic
            suspend fun impossible(func: suspend () -> Unit) {
                try {
                    func()
                    fail("should have failed")
                } catch (c: Cannot) {
                    println("As expected: $c")
                }
            }
        }
    }
}