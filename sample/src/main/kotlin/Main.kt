import external.FileStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() = Main().go()

class Main : CoroutineScope {
    override val coroutineContext = Dispatchers.Default
    private val store = FileStore(coroutineContext, File("store"))
    fun go() {
        runBlocking {
            store.makeBird("robin")
            println("Hello world")
        }
    }
}
