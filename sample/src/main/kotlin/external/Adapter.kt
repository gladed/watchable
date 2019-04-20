package external

import kotlinx.coroutines.Job
import logic.Logic
import model.Bird
import store.FileStore
import util.toInflater
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Integrate local interfaces into a Logic object. */
class Adapter(context: CoroutineContext, root: File) : Logic(
    context + Job(),
    FileStore(root, ".json").inflate(Bird.serializer().toInflater())
)