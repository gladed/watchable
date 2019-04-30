package logic

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import model.Chirp
import io.gladed.watchable.store.Store

/** Perform search operations based on available stores. */
@UseExperimental(FlowPreview::class)
open class Operations(private val chirps: Store<Chirp>) {
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
}
