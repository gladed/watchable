package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.store.HoldingStore
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
import model.Bird
import model.Chirp
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
@UseExperimental(FlowPreview::class)
class Logic(
    context: CoroutineContext,
    birdStore: Store<Bird>,
    chirpStore: Store<Chirp>,
    val ops: Operations
) : CoroutineScope {
    override val coroutineContext = context + Job()

    /** Return [Scoped] with stores available that live as long as [scope]. */
    fun scoped(scope: CoroutineScope) = object : Scoped {
        override val birds by lazy { this@Logic.birds.create(scope) }
        override val chirps by lazy { this@Logic.chirps.create(scope) }
        override val ops = this@Logic.ops
    }

    /** A context for program activity within a particular [CoroutineScope]. */
    interface Scoped {
        val birds: Store<Bird>
        val chirps: Store<Chirp>
        val ops: Operations
    }

    /** A [HoldingStore] for birds, from which new scope-specific stores can be derived. */
    private val birds: HoldingStore<Bird> = holding(birdStore) { bird ->
        onCreate {
            if (bird.following.isNotEmpty()) {
                cannot("create a bird following other birds")
            }
        }

        onRemove {
            // Delete any chirps from this bird using a throwaway scope
            coroutineScope {
                val chirps = chirps.create(this)
                ops.chirpsForBird(bird.id).collect { chirpId ->
                    chirps.remove(chirpId)
                }
            }
        }

        onWatcher(watch(bird.following, INLINE) {
            if (!it.isInitial) it.simple.forEach { simple ->
                simple.add?.also { birdId ->
                    // Just get the bird to make sure it's there
                    birdStore.get(birdId)
                }
            }
        })
    }

    /** A [HoldingStore] for chirps, from which new scope-specific stores can be derived. */
    private val chirps = holding(chirpStore) { chirp ->
        onCreate {
            if (chirp.text.length > MAX_CHIRP_LENGTH) {
                cannot("chirp with text longer than $MAX_CHIRP_LENGTH")
            }
            // Ensure the originator is valid
            birdStore.get(chirp.from)
        }

        onWatcher(watch(chirp.reactions, INLINE) { change ->
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
        })
    }

    companion object {
        const val MAX_REACTION_LENGTH = 6
        const val MAX_CHIRP_LENGTH = 320
    }
}
