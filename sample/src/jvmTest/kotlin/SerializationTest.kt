import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableMap
import io.gladed.watchable.toWatchableSet
import io.gladed.watchable.toWatchableValue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import util.Thing

class SerializationTest {
    private val thing = Thing(
        "hi".toWatchableValue(),
        listOf("hi").toWatchableList(),
        mapOf("hi" to "there").toWatchableMap(),
        setOf("hi").toWatchableSet()
    )

    @Test
    fun `serialize thing`() {
        val string = Json.encodeToString(thing)
        val parsed: Thing = Json.decodeFromString(string)
        assertEquals(thing, parsed)
    }
}
