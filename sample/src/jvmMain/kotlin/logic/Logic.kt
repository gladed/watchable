package logic

import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.simple
import io.gladed.watchable.store.HoldBuilder
import io.gladed.watchable.store.HoldingStore
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.cannot
import io.gladed.watchable.store.holding
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
    private val birdStore: Store<Bird>,
    private val chirpStore: Store<Chirp>,
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
    private val birds = holding(birdStore) { holdBird(it) }

    private fun HoldBuilder.holdBird(bird: Bird) {
        onCreate {
            println("CREATING $bird")
            if (bird.following.isNotEmpty()) {
                cannot("create a bird following other birds")
            }
        }

        onRemove {
            println("REMOVING $bird")
            coroutineScope {
                with(scoped(this)) {
                    // Delete any chirps from this bird
                    ops.chirpsForBird(bird.id).collect {
                        chirps.remove(it)
                    }

                    // Also for any bird following this one trash it from the list
                    ops.followersOf(bird.id).collect { followerId ->
                        birds.get(followerId).following.also { println("From $followerId's follow list $it removing ${bird.id}") }.remove(bird.id)
                    }
                }
            }
        }

        onWatcher(simple(bird.following, INLINE) {
            if (!it.isInitial) {
                it.add?.also { birdId ->
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

        onWatcher(simple(chirp.reactions, INLINE) { simple ->
            if (!simple.isInitial) {
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
