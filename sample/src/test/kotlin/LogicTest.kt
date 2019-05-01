/*
 * (c) Copyright 2019 Glade Diviney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.gladed.watchable.store.MemoryStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import logic.Logic
import logic.Operations
import model.Bird
import model.Chirp
import model.MutableBird
import model.MutableChirp
import org.junit.Assert.assertEquals
import org.junit.Test
import test.impossible
import test.runTest

@UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
class LogicTest {
    private val robin = Bird(name = "robin")
    private val chirp = Chirp(from = robin.id, text = "hi")
    private val wren = Bird(name = "wren")
    private val birdStore = MemoryStore<Bird>("bird")
    private val chirpStore = MemoryStore<Chirp>("chirp")

    /** Run a test with a `logic` object backed by RAM. */
    private fun test(func: suspend Context.() -> Unit) {
        runTest {
            (object : Context, TestCoroutineScope by this {
                override val logic = Logic(coroutineContext,
                    birdStore.inflate(MutableBird),
                    chirpStore.inflate(MutableChirp),
                    Operations(chirpStore))
            }).func()
        }
    }

    @Test fun `write bird to store`() = test {
        val bird = MutableBird(robin)
        logic.birds.create(this).put(robin.id, bird)
        assertEquals(robin, birdStore.get(robin.id))
    }

    @Test fun `update bird in store`() = test {
        val bird = MutableBird.inflate(robin)
        logic.birds.create(this).put(robin.id, bird)
        bird.name.set("robin2")
        assertEquals("robin2", birdStore.get(robin.id).name)
    }

    @Test fun `cannot update deleted bird`() = test {
        val bird = MutableBird(robin)
        val birds = logic.birds.create(this)
        birds.put(robin.id, bird)
        birds.delete(robin.id)
        // We can change it but it makes no difference
        bird.name.set("robin2")

        impossible {
            birdStore.get(robin.id)
        }
    }

    @Test fun `cannot follow bird that does not exist`() = test {
        val bird = MutableBird(robin)
        logic.birds.create(this).put(robin.id, bird)

        impossible {
            bird.following.add(wren.id)
        }
    }

    @Test fun `bird sends a chirp`() = test {
        coroutineScope {
            logic.birds.create(this).put(robin.id, MutableBird(robin))
            logic.chirps.create(this).put(chirp.id, MutableChirp(chirp))
        }

        coroutineScope {
            assertEquals(chirp, logic.chirps.create(this).get(chirp.id).toChirp())
        }
    }

    @Test fun `unknown bird cannot chirp`() = test {
        impossible {
            logic.chirps.create(this).put(chirp.id, MutableChirp(chirp))
        }
    }

    @Test fun `delete bird causes chirp delete`() = test {
        val birds = logic.birds.create(this)
        val chirps = logic.chirps.create(this)

        birds.put(robin.id, MutableBird(robin))
        chirps.put(chirp.id, MutableChirp(chirp))
        birds.delete(robin.id)

        impossible {
            chirps.get(chirp.id)
        }
    }

    @Test fun `cannot create with bad followers`() = test {
        val birds = logic.birds.create(this)
        impossible {
            birds.put(robin.id, MutableBird(robin.copy(following = listOf("bad"))))
        }
    }

    companion object {
        private interface Context : TestCoroutineScope {
            val logic: Logic
        }
    }
}
