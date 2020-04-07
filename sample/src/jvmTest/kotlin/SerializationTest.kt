import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.WatchableSet
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableMap
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.toWatchableValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import util.WatchableListSerializer
import util.WatchableMapSerializer
import util.WatchableSetSerializer
import util.WatchableValueSerializer

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

class SerializationTest {
    private val thing = Thing(
        "hi".toWatchableValue(),
        listOf("hi").toWatchableList(),
        mapOf("hi" to "there").toWatchableMap(),
        setOf("hi").toWatchableSet())

    @OptIn(UnstableDefault::class)
    @Test
    fun `serialize thing`() {
        val string = Json.stringify(Thing.serializer(), thing)
        val parsed = Json.parse(Thing.serializer(), string)
        assertEquals(thing, parsed)
    }
}