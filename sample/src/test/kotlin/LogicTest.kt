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
import io.gladed.watchable.store.transform
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
    private val robin = MutableBird(Bird(name = "robin"))
    private val chirp = MutableChirp(Chirp(from = robin.id, text = "hi"))
    private val wren = MutableBird(Bird(name = "wren"))
    private val birdStore = MemoryStore<Bird>("bird")
    private val chirpStore = MemoryStore<Chirp>("chirp")

    private class Context(
        val scope: TestCoroutineScope,
        val logic: Logic
    ): TestCoroutineScope by scope {
        override val coroutineContext = scope.coroutineContext
        val birds by lazy { logic.birds.create(this) }
        val chirps by lazy { logic.chirps.create(this) }
    }

    /** Run a test with a `logic` object backed by RAM. */
    private fun test(func: suspend Context.() -> Unit) {
        runTest {
            val logic = Logic(coroutineContext,
                birdStore.transform(MutableBird),
                chirpStore.transform(MutableChirp),
                Operations(chirpStore))
            Context(this, logic).func()
        }
    }

    @Test fun `write bird to store`() = test {
        birds.put(robin.id, robin)
        assertEquals(robin.toBird(), birdStore.get(robin.id))
    }

    @Test fun `update bird in store`() = test {
        birds.put(robin.id, robin)
        robin.name.set("robin2")
        assertEquals("robin2", birdStore.get(robin.id).name)
    }

    @Test fun `cannot update deleted bird`() = test {
        birds.put(robin.id, robin)
        birds.remove(robin.id)
        // We can change it but it makes no difference
        robin.name.set("robin2")

        impossible {
            birdStore.get(robin.id)
        }
    }

    @Test fun `cannot follow bird that does not exist`() = test {
        birds.put(robin.id, robin)

        impossible {
            robin.following.add(wren.id)
        }
    }

    @Test fun `bird sends a chirp`() = test {
        coroutineScope {
            birds.put(robin.id, robin)
            logic.chirps.create(this).put(chirp.id, chirp)
        }

        coroutineScope {
            assertEquals(chirp, logic.chirps.create(this).get(chirp.id))
        }
    }

    @Test fun `unknown bird cannot chirp`() = test {
        impossible {
            logic.chirps.create(this).put(chirp.id, chirp)
        }
    }

    @Test fun `delete bird causes chirp delete`() = test {
        birds.put(robin.id, robin)
        chirps.put(chirp.id, chirp)
        birds.remove(robin.id)

        impossible {
            chirps.get(chirp.id)
        }
    }

    @Test fun `cannot create with bad followers`() = test {
        robin.following { add("bad") }
        impossible {
            birds.put(robin.id, robin)
        }
    }

    @Test fun `cannot chirp a long text`() = test {
        birds.put(robin.id, robin)
        val badChirp = chirp.copy(text = "hi".repeat(300))
        impossible {
            chirps.put(chirp.id, badChirp)
        }
    }

    @Test fun `bird can react to another chirp`() = test {
        birds.put(robin.id, robin)
        birds.put(wren.id, wren)
        chirps.put(chirp.id, chirp)
        chirp.reactions {
            put(wren.id, "+1")
        }
    }

    @Test fun `bird cannot react to own chirp`() = test {
        birds.put(robin.id, robin)
        birds.put(wren.id, wren)
        chirps.put(chirp.id, chirp)
        impossible {
            chirp.reactions {
                put(robin.id, "+1")
            }
        }
    }

    @Test fun `cannot send long reaction`() = test {
        birds.put(robin.id, robin)
        birds.put(wren.id, wren)
        chirps.put(chirp.id, chirp)
        impossible {
            chirp.reactions {
                put(wren.id, "+100".repeat(100))
            }
        }
    }
}
