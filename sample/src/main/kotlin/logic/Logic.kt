package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.store.Hold
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.cannot
import io.gladed.watchable.store.holding
import io.gladed.watchable.store.plus
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import model.MutableBird
import model.MutableChirp
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
@UseExperimental(FlowPreview::class)
class Logic(
    context: CoroutineContext,
    birdStore: Store<MutableBird>,
    chirpStore: Store<MutableChirp>,
    val ops: Operations
) : CoroutineScope {
    override val coroutineContext = context + Job()

    val birds = birdStore.holding(coroutineContext) { bird ->
            Hold(onRemove = {
                // Delete any chirps from this bird using a throwaway scope
                coroutineScope {
                    val chirps = chirps.create(this)
                    ops.chirpsForBird(bird.id).collect { chirpId ->
                        chirps.delete(chirpId)
                    }
                }
            }, onCreate = {
                if (bird.following.isNotEmpty()) {
                    cannot("create a bird with followers")
                }
            }) + watch(MutableBird.extract(bird)) {
                if (!it.isInitial) {
                    // For any non-initial change, store the current version
                    birdStore.put(bird.id, bird)
                }
            } + watch(bird.following, INLINE) {
                if (!it.isInitial) it.simple.forEach { simple ->
                    simple.add?.also { birdId ->
                        // Just get the bird to make sure it's there
                        birdStore.get(birdId)
                    }
                }
            }
        }

    val chirps = chirpStore
        .holding(coroutineContext) { chirp ->
            Hold(
                onStart = { birdStore.get(chirp.from) },
                onCreate = {
                    if (chirp.text.length > MAX_CHIRP_LENGTH) {
                        cannot("chirp with text longer than $MAX_CHIRP_LENGTH")
                    }
                }
            ) + watch(chirp.reactions, INLINE) { change ->
                if (!change.isInitial) change.simple.forEach { simple ->
                    simple.add?.also { addValue ->
                        if (chirp.from == simple.key) {
                            cannot("react to own chirp")
                        }

                        // React initiator must exist
                        birdStore.get(simple.key)
                        if (addValue.length > MAX_REACTION_LENGTH) {
                            cannot("react with text longer than $MAX_REACTION_LENGTH")
                        }
                    }
                }
            }
        }

    companion object {
        const val MAX_REACTION_LENGTH = 6
        const val MAX_CHIRP_LENGTH = 320
    }
}
