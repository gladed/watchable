package model

import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.group
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.watchableListOf
import kotlinx.serialization.Serializable
import store.Inflater
import java.util.UUID

@Serializable
data class Bird(
    /** A unique identifier for this person. */
    val id: String = UUID.randomUUID().toString(),

    /** The name of this person. */
    val name: String,

    /** [Bird.id]s that this bird is following. */
    val following: List<String> = listOf()
)

/** Mutable form of [Bird]. */
data class MutableBird(
    val id: String = UUID.randomUUID().toString(),
    val name: WatchableValue<String>,
    val following: WatchableList<String> = watchableListOf()
) {
    val watchables = group(name, following)

    companion object : Inflater<Bird, MutableBird> {
        override fun inflate(value: Bird) = with(value) {
            MutableBird(id, name.toWatchableValue(), following.toWatchableList())
        }
        override fun deflate(value: MutableBird) = with(value) {
            Bird(id, name.value, following.toList())
        }
    }
}
