import io.gladed.watchable.MapChange
import io.gladed.watchable.SetChange
import io.gladed.watchable.bind
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class TwoWayBindTest {
    private val set = watchableSetOf<String>()
    private val map = watchableMapOf(1 to "1")

    @Test @Ignore
    fun `bind set with map`() = runTest {
        bind(set, map, { mapChange ->
            println("Handling map change $mapChange")
            when (mapChange) {
                is MapChange.Initial -> addAll(mapChange.map.values)
                is MapChange.Put -> {
                    mapChange.remove?.also { remove(it) }
                    add(mapChange.add)
                }
                is MapChange.Remove -> remove(mapChange.remove)
            }
        }) { setChange ->
            println("Handling set change $setChange")
            when (setChange) {
                is SetChange.Initial -> { } // Ignore
                is SetChange.Add -> setChange.add.forEach { this[it.toInt()] = it }
                is SetChange.Remove -> setChange.remove.forEach { remove(it.toInt()) }
            }
        }
        assertEquals(setOf("1"), set)
        println("Adding 2 to set")
        set { add("2") }
        println("Asserting 2 is in set")
        assertEquals("2", map[2])
    }
}
