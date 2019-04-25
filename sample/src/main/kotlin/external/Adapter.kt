package external

import logic.Logic
import model.Bird
import model.Chirp
import logic.Operations
import store.FileStore
import store.Store
import util.inflate
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Implement a Logic object backed by real adapters. */
object Adapter {

    class FileOperations(chirps: Store<Chirp>) : Operations(chirps)

    fun createLogic(context: CoroutineContext, root: File): Logic {
        val birds = FileStore(root, "bird", JSON_SUFFIX).inflate(Bird.serializer())
        val chirps = FileStore(root, "chirp", JSON_SUFFIX).inflate(Chirp.serializer())
        return Logic(context, birds, chirps, FileOperations(chirps))
    }

    private const val JSON_SUFFIX = "json"
}
