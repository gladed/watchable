package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.Watcher
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import model.Bird
import model.Chirp
import model.MutableBird
import model.Operations
import store.Store
import store.onRemove
import store.scoping
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
@UseExperimental(FlowPreview::class)
class Logic(
    context: CoroutineContext,
    private val birdStore: Store<Bird>,
    chirpStore: Store<Chirp>,
    private val ops: Operations
) : CoroutineScope {
    override val coroutineContext = context + Job()

    val birds = birdStore
        .inflate(MutableBird)
        .scoping(coroutineContext) { bird ->
            bird.birdWatcher().apply { start() }.onRemove {
                // Delete any chirps from this bird.
                coroutineScope {
                    val chirps = chirps.create(this)
                    ops.chirpsForBird(bird.id).collect { chirpId ->
                        chirps.delete(chirpId)
                    }
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
            if (!it.isInitial) it.simple.forEach { simple ->
                simple.add?.also { addKey ->
                    // Just get the bird to make sure it's there
                    birdStore.get(addKey)
                }
            }
        }

    val chirps = chirpStore
        .scoping(coroutineContext) { it.chirpWatcher().apply { start() } }

    private suspend fun Chirp.chirpWatcher(): Watcher = object : Watcher {
        override suspend fun start() {
            birdStore.get(from) // Ensure the originating bird is present
        }
        override fun cancel() { }
        override suspend fun stop() { }
    }
}
