package logic

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import model.Chirp
import io.gladed.watchable.store.Store
import kotlinx.coroutines.flow.Flow
import model.Bird

/**
 * Perform search operations based on available stores.
 *
 * The default operations are probably not very scalable and should be overridden.
 */
@UseExperimental(FlowPreview::class)
open class Operations(private val chirps: Store<Chirp>, private val birds: Store<Bird>) {
    /**
     * Returns the ids of all chirps issued by a bird.
     *
     * The default operation iterates through all chirps, comparing IDs. This will get pretty slow if there are many.
     */
    open fun chirpsForBird(birdId: String) =
        chirps.keys()
            .map { chirps.get(it) }
            .filter { it.from == birdId }
            .map { it.id }


    /**
     * Chirps that related to birdId
     */
    open suspend fun relatedChirps(birdId: String): Flow<String> {
        val bird = birds.get(birdId)
        return chirps.keys()
            .map { chirps.get(it) }
            .filter { it.from == birdId || bird.following.contains(it.from) }
            .map { it.id }
    }

    /** Return all birds matching name. */
    open fun birdsWithName(name: String) =
        birds.keys().map { birds.get(it) }.filter {
            it.name.value.toLowerCase().contains(name.toLowerCase()) ||
                name.toLowerCase().contains(it.name.value.toLowerCase())
        }

    /** Birds following this one. */
    open fun followersOf(birdId: String) =
        birds.keys()
            .map { birds.get(it) }
            .filter { it.following.contains(birdId) }
            .map { it.id }
}
