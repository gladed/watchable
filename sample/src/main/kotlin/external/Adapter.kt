package external

import kotlinx.coroutines.Job
import logic.Logic
import model.Bird
import store.FileStore
import util.toInflater
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Implement a Logic object backed by real adapters. */
class Adapter(context: CoroutineContext, root: File) : Logic(
    context + Job(),
    FileStore(root, ".json").inflate(Bird.serializer().toInflater())
)
