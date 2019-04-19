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
            assertEquals("robin", bird.name)
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
            assertEquals("robin", newBird?.name)
        }
    }

    @Test fun `change bird content`() {
//        val (robinId, wrenId) = withFileStore {
//            val robin = makeBird("robin")
//            val wren = makeBird("wren")
//            robin.following.use {
//                add(wren.id)
//            }
//            robin.id to wren.id
//        }
//
//        // New file store instance must load from disk
//        withFileStore {
//            val robin = findBird(robinId)!!
//            assertEquals(listOf(wrenId), robin.following)
//        }
    }
}
