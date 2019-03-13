package model

import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.watchableListOf
import kotlinx.serialization.Serializable
import util.WatchableListSerializer
import util.WatchableValueSerializer
import java.util.UUID

@Serializable
data class Bird(
    /** A unique identifier for this person. */
    val id: String = UUID.randomUUID().toString(),

    /** The name of this person. */
    @Serializable(with = WatchableValueSerializer::class)
    val name: WatchableValue<String>,

    /** [Bird.id]s that this bird is following. */
    @Serializable(with = WatchableListSerializer::class)
    val following: WatchableList<String> = watchableListOf(),

    /** The list of [Chirp.id]s originating from this bird. */
    @Serializable(with = WatchableListSerializer::class)
    val chirps: WatchableList<String> = watchableListOf()
)