import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = Main().go()

class Main : CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    fun go() {
        runBlocking {
            delay(20)
            println("Hello world")
        }
    }
}