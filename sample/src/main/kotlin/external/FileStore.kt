package external

import io.gladed.watchable.watch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import model.Bird
import store.Store
import java.io.File
import kotlin.coroutines.coroutineContext

@UseExperimental(ObsoleteCoroutinesApi::class)
class FileStore(dir: File) : Store, CoroutineScope {
    private val birdsDir = File(dir, BIRDS_DIR)
    private val birds = mutableMapOf<String, Handle<Bird>>()
    override val coroutineContext = newSingleThreadContext("FileStore") + Job()

    override suspend fun makeBird(name: String): Bird {
        val bird = Bird(name = watchableValueOf(name))
        birds[bird.id] = newHandle(bird)
        save(bird)
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
        watch(bird.name) { launch { save(bird) } }
        watch(bird.following) { launch { save(bird) } }
        watch(bird.chirps) { launch { save(bird) } }
    }

    private suspend fun save(bird: Bird) {
        withContext(coroutineContext) {
            val birdDir = File(birdsDir, bird.id)
            birdDir.mkdirs()
            File(birdDir, BIRD_FILE_NAME).bufferedWriter().use {
                it.write(Json.stringify(Bird.serializer(), bird)
                    .also { println("Saving $it")})
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
        const val BIRD_FILE_NAME = "bird.json"
        const val BIRDS_DIR = "birds"
        private suspend fun callerContext() = coroutineContext
    }
}
