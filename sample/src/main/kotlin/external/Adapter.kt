package external

import logic.Logic
import model.Bird
import model.Chirp
import logic.Operations
import model.MutableBird
import store.FileStore
import util.inflate
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Construct real-world objects for use by the application. */
object Adapter {

    /** Create a Logic object based on a folder on disk. */
    fun createLogic(context: CoroutineContext, root: File): Logic {
        val birds = FileStore(root, "bird", JSON_SUFFIX).inflate(Bird.serializer())
        val chirps = FileStore(root, "chirp", JSON_SUFFIX).inflate(Chirp.serializer())
        return Logic(context, birds.inflate(MutableBird), chirps, Operations(chirps))
    }

    private const val JSON_SUFFIX = "json"
}