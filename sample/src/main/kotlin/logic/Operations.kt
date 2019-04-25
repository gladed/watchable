package logic

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import model.Chirp
import store.Store

/** Searching and other operations. */
@UseExperimental(FlowPreview::class)
open class Operations(private val chirps: Store<Chirp>) {
    open fun chirpsForBird(birdId: String) =
        chirps.keys()
            .map { chirps.get(it) }
            .filter { it.from == birdId }
            .map { it.id }
}
