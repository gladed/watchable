package util

import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.WatchableSet
import io.gladed.watchable.WatchableValue
import kotlinx.serialization.Serializable

/**
 * A class for unit testing all Watchable collection serializers.
 * It's here in main so that kotlinx will generate a serializer for it.
 */
@Serializable
data class Thing(
    @Serializable(with = WatchableValueSerializer::class)
    val value: WatchableValue<String>,
    @Serializable(with = WatchableListSerializer::class)
    val list: WatchableList<String>,
    @Serializable(with = WatchableMapSerializer::class)
    val map: WatchableMap<String, String>,
    @Serializable(with = WatchableSetSerializer::class)
    val set: WatchableSet<String>
)
