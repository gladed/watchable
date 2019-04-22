import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import logic.Logic
import model.Bird
import model.Chirp
import model.MutableBird
import model.Operations
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import store.Cannot
import store.MemoryStore

@UseExperimental(FlowPreview::class)
class LogicTest {
    private val robin = Bird(name = "robin")
    private val chirp = Chirp(from = robin.id, text = "hi")
    private val wren = Bird(name = "wren")
    private val birdStore = MemoryStore<Bird>("bird")
    private val chirpStore = MemoryStore<Chirp>("chirp")

    private val ops = object : Operations {
        override fun chirpsForBird(birdId: String) =
            flow {
                chirpStore.keys().forEach { chirpId ->
                    if (chirpStore.get(chirpId).from == birdId) {
                        emit(chirpId)
                    }
                }
            }
        }


    /** Run a test with a `logic` object backed by RAM. */
    private fun test(func: suspend Context.() -> Unit) {
        runTest {
            (object : Context, TestCoroutineScope by this {
                override val logic = Logic(coroutineContext, birdStore, chirpStore, ops)
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

    @Test fun `bird sends a chirp`() = test {
        inScope {
            val bird = MutableBird.inflate(robin)
            logic.birds.create(this).put(robin.id, bird)
            logic.chirps.create(this).put(chirp.id, chirp)
        }

        inScope {
            assertEquals(chirp, logic.chirps.create(this).get(chirp.id))
        }
    }

    @Test fun `unknown bird cannot chirp`() = test {
        inScope {
            impossible {
                logic.chirps.create(this).put(chirp.id, chirp)
            }
        }
    }

    @Test fun `delete bird deletes chirp`() = test {
        inScope {
            val birds = logic.birds.create(this)
            val chirps = logic.chirps.create(this)

            birds.put(robin.id, MutableBird.inflate(robin))
            chirps.put(chirp.id, chirp)
            birds.delete(robin.id)

            impossible {
                chirps.get(chirp.id)
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
