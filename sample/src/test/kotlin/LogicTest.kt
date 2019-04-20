import logic.Logic
import model.Bird
import model.MutableBird
import org.junit.Assert.assertEquals
import org.junit.Test
import store.MemoryStore

class LogicTest {
    private val robin = Bird(name = "robin")
    private val wren = Bird(name = "wren")
    private val birdStore = MemoryStore<Bird>()

    interface Context : TestCoroutineScope {
        val logic: Logic
    }

    fun test(func: suspend Context.() -> Unit) {
        runTest {
            (object : Context, TestCoroutineScope by this {
                override val logic = Logic(coroutineContext, birdStore)
            }).func()
        }
    }

    @Test fun `write bird to store`() = test {
        inScope {
            logic.birds.create(this).put(robin.id, MutableBird.inflate(robin))
        }
        assertEquals(robin, birdStore.get(robin.id))
    }

    @Test fun `update bird in store`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            logic.birds.create(this).put(robin.id, bird)
            assertEquals(robin, birdStore.get(robin.id))
            birdStore.delete(robin.id) // Delete from backing store to see if it gets re-written

            bird.following.add(wren.id)
            trigger() // Let stuff happen
            assertEquals(wren.id, birdStore.get(robin.id).following[0])
        }
    }
}