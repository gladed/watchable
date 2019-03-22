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

package external

import io.gladed.watchable.batch
import io.gladed.watchable.group
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import model.Bird
import store.Store
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@UseExperimental(ObsoleteCoroutinesApi::class)
class FileStore(
    override val coroutineContext: CoroutineContext = newSingleThreadContext("FileStore") + Job(),
    dir: File,
    private val fileDelayMillis: Long = DEFAULT_FILE_DELAY_MILLIS
) : Store, CoroutineScope {

    private val birdsDir = File(dir, BIRDS_DIR)
    private val birds = mutableMapOf<String, Handle<Bird>>()

    override suspend fun makeBird(name: String): Bird {
        val bird = Bird(name = watchableValueOf(name))
        birds[bird.id] = newHandle(bird)
        watch(bird)
        return bird
    }

    override suspend fun findBird(id: String): Bird? {
        val handle: Handle<Bird>? = birds[id]?.also { it += coroutineContext}
            ?: load(id)?.let { newHandle(it) }
        return handle?.value
    }

    /** Create a handle for the bird, shutting it down when we're finished with it. */
    private suspend fun newHandle(bird: Bird): Handle<Bird> {
        watch(bird)
        return Handle(callerContext(), bird)
            .also { it.invokeOnCompletion { birds -= bird.id } }
    }

    private fun watch(bird: Bird) {
        // Only save this maximum once per fileDelayMillis
        batch(group(bird.name, bird.following), fileDelayMillis) {
            save(bird)
        }
    }

    private suspend fun save(bird: Bird) {
        withContext(coroutineContext) {
            val birdDir = File(birdsDir, bird.id)
            birdDir.mkdirs()
            File(birdDir, BIRD_FILE_NAME).bufferedWriter().use {
                it.write(Json.stringify(Bird.serializer(), bird))
            }
        }
    }

    private suspend fun load(id: String): Bird? =
        withContext(coroutineContext) {
            val file = File(File(birdsDir, id), BIRD_FILE_NAME)
            if (!file.isFile) null else {
                // Create a new storable bird in the originating context
                Json.parse(Bird.serializer(), file.bufferedReader().use { it.readText() })
            }
        }

    companion object {
        // Wait .5 seconds before persisting to disk to prevent thrash
        const val DEFAULT_FILE_DELAY_MILLIS = 500L
        const val BIRD_FILE_NAME = "bird.json"
        const val BIRDS_DIR = "birds"
        private suspend fun callerContext() = coroutineContext
    }
}
