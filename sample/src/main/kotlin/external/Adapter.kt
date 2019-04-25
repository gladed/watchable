package external

import logic.Logic
import logic.Operations
import model.Bird
import model.Chirp
import model.MutableBird
import store.FileStore
import store.cached
import util.inflate
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Construct real-world objects for use by the application. */
object Adapter {

    /** Create a Logic object based on a folder on disk. */
    fun createLogic(context: CoroutineContext, root: File): Logic {
        val birds = FileStore(root, "bird", JSON_SUFFIX).inflate(Bird.serializer()).cached(context)
        val chirps = FileStore(root, "chirp", JSON_SUFFIX).inflate(Chirp.serializer()).cached(context)
        return Logic(context, birds.inflate(MutableBird), chirps, Operations(chirps))
    }

    private const val JSON_SUFFIX = "json"
}
