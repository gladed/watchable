import external.Adapter
import io.gladed.watchable.toWatchableValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import model.MutableBird
import java.io.File

fun main() = Main().go()

class Main : CoroutineScope {
    override val coroutineContext = Dispatchers.Default
    private val logic = Adapter.createLogic(coroutineContext, File("store"))
    fun go() {
        runBlocking {
            println("Hello world")
            val store = logic.birds.create(this)
            val robin = MutableBird(name = "robin".toWatchableValue())
            store.put(robin.id, robin)
        }
    }
}
