package model

import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableValue
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Bird(
    /** A unique identifier for this person. */
    override val id: String = UUID.randomUUID().toString(),

    /** The name of this person. */
    val name: String,

    /** [Bird.id]s that this bird is following. */
    val following: List<String> = listOf()
) : Identified

interface Identified {
    val id: String
}

interface Deflateable<T> {
    fun deflate(): T
}

interface Inflater<T, U: Deflateable<T>> {
    fun T.inflate(): U
}

data class LiveBird(
    override val id: String,
    val name: WatchableValue<String>,
    val following: WatchableList<String>): Deflateable<Bird>, Identified {

    override fun deflate() = Bird(id, name.value, following.toList())

    companion object : Inflater<Bird, LiveBird> {
        override fun Bird.inflate() = LiveBird(id, name.toWatchableValue(), following.toWatchableList())
    }
}
