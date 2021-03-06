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
import kotlinx.coroutines.test.TestCoroutineScope
import logic.Logic
import logic.Operations
import model.Bird
import model.Chirp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogicTest {
    private val robin = Bird("robin")
    private val chirp = Chirp(from = robin.id, text = "hi")
    private val wren = Bird("wren")
    private val birdStore = MemoryStore<Bird>("bird")
    private val chirpStore = MemoryStore<Chirp>("chirp")

    private class Context constructor(
        val scope: TestCoroutineScope,
        val logic: Logic
    ): TestCoroutineScope by scope, Logic.Scoped by logic.scoped(scope)

    /** Run a test with a `logic` object backed by RAM. */
    private fun test(func: suspend Context.() -> Unit) {
        runTest {
            val logic = Logic(coroutineContext, birdStore, chirpStore, Operations(chirpStore, birdStore))
            Context(this, logic).func()
        }
    }

    @Test fun `write bird to store`() = test {
        birds.put(robin.id, robin)
        assertEquals(robin, birdStore.get(robin.id))
    }

    @Test fun `update bird in store`() = test {
        birds.put(robin.id, robin)
        robin.name.set("robin2")
        assertEquals("robin2", birdStore.get(robin.id).name.value)
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
        birds.put(robin.id, robin)
        chirps.put(chirp.id, chirp)
        assertEquals(chirp, chirps.get(chirp.id))
    }

    @Test fun `unknown bird cannot chirp`() = test {
        impossible {
            chirps.put(chirp.id, chirp)
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

    @Test fun `delete bird causes follower delete`() = test {
        birds.put(robin.id, robin)
        birds.put(wren.id, wren)
        wren.following.add(robin.id)
        birds.remove(robin.id)
        assertTrue(wren.following.isEmpty())
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
}
