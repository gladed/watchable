package model

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

/** Searching and other operations */
@UseExperimental(FlowPreview::class)
interface Operations {
    fun chirpsForBird(birdId: String): Flow<String>
}
