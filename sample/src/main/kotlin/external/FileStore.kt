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

import io.gladed.watchable.group
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
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

    override suspend fun findBird(id: String): Bird? =
        (birds[id]?.also { it += coroutineContext }
            ?: load(id)?.let { newHandle(it) })
            ?.value

    /** Create a handle for the bird, shutting it down when we're finished with it. */
    private suspend fun newHandle(bird: Bird): Handle<Bird> {
        watch(bird)
        return Handle(callerContext(), bird)
            .also { it.invokeOnCompletion { birds -= bird.id } }
    }

    private suspend fun watch(bird: Bird) {
        // Only save this maximum once per fileDelayMillis
        group(bird.name, bird.following).batch(this, fileDelayMillis) {
            save(bird)
        }
    }

    private fun save(bird: Bird) {
        val birdDir = File(birdsDir, bird.id).apply { mkdirs() }
        File(birdDir, BIRD_FILE_NAME).writeJson(Bird.serializer(), bird)
    }

    private fun load(id: String): Bird? =
        File(File(birdsDir, id), BIRD_FILE_NAME).takeIf { it.isFile }?.readJson(Bird.serializer())

    companion object {
        // Wait .5 seconds before persisting to disk to prevent thrash
        const val DEFAULT_FILE_DELAY_MILLIS = 500L
        const val BIRD_FILE_NAME = "bird.json"
        const val BIRDS_DIR = "birds"

        /** Return this suspending function's current context. */
        private suspend fun callerContext() = coroutineContext

        private fun <T : Any> File.writeJson(strategy: SerializationStrategy<T>, value: T) {
            bufferedWriter().use { it.write(Json.stringify(strategy, value)) }
        }

        private fun <T : Any> File.readJson(strategy: DeserializationStrategy<T>): T =
            bufferedReader().use { (Json.parse(strategy, it.readText())) }
    }
}
