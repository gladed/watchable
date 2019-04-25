package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import model.Bird
import model.Chirp
import model.MutableBird
import store.Hold
import store.Store
import store.holding
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
@UseExperimental(FlowPreview::class)
class Logic(
    context: CoroutineContext,
    birdStore: Store<Bird>,
    chirpStore: Store<Chirp>,
    private val ops: Operations
) : CoroutineScope {
    override val coroutineContext = context + Job()

    val birds = birdStore.inflate(MutableBird).holding(coroutineContext) { bird ->
            Hold(onRemove = {
                // Delete any chirps from this bird.
                coroutineScope {
                    val chirps = chirps.create(this)
                    ops.chirpsForBird(bird.id).collect { chirpId ->
                        chirps.delete(chirpId)
                    }
                }
            }) + watch(bird.watchables) {
                if (!it.isInitial) {
                    // For any non-initial change, store the current version
                    birdStore.put(bird.id, MutableBird.deflate(bird))
                }
            } + watch(bird.following, INLINE) {
                if (!it.isInitial) it.simple.forEach { simple ->
                    simple.add?.also { addKey ->
                        // Just get the bird to make sure it's there
                        birdStore.get(addKey)
                    }
                }
            }
        }

    val chirps = chirpStore
        .holding(coroutineContext) { chirp ->
            Hold(onStart = { birdStore.get(chirp.from) })
        }
}
