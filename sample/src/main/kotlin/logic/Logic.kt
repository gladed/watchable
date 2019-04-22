package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.Watcher
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import model.Bird
import model.Chirp
import model.Operations
import model.MutableBird
import store.Store
import store.scope
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
class Logic(
    context: CoroutineContext,
    private val birdStore: Store<Bird>,
    private val chirpStore: Store<Chirp>,
    val birdOps: Operations
) : CoroutineScope {
    override val coroutineContext = context + Job()

    val birds = birdStore
        .inflate(MutableBird)
        .scope(coroutineContext) {
            birdWatcher().apply { start() }
        }

    val chirps = chirpStore
        .scope(coroutineContext) {
            chirpWatcher().apply { start() }
        }

    private suspend fun Chirp.chirpWatcher(): Watcher {
        return object : Watcher {
            override suspend fun start() {
                println("${Thread.currentThread().name}: checking bird $from")
                birdStore.get(from) // Make sure it's there
            }

            override fun cancel() {
            }

            override suspend fun stop() {
            }
        }
    }

    private fun MutableBird.birdWatcher(): Watcher =
        watch(watchables) {
            if (!it.isInitial) {
                // For any non-initial change, store.
                birds.back.put(id, this@birdWatcher)
            }
        } + watch(following, INLINE) {
            if (!it.isInitial) {
                it.simple.forEach { simple ->
                    simple.add?.also { addKey ->
                        // Just get the bird to make sure it's there
                        birdStore.get(addKey)
                    }
                }
            }
        }
}
