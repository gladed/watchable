import external.FileStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() = Main().go()

class Main : CoroutineScope {
    override val coroutineContext = Dispatchers.Default
    val store = FileStore(coroutineContext, File("store"))
    fun go() {
        runBlocking {
            store.makeBird("tweety")
            println("Hello world")
        }
    }
}