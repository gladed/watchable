package external

import io.gladed.watchable.Watcher
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import model.Bird
import util.FileStore
import util.inflate
import util.json
import util.scope
import java.io.File
import kotlin.coroutines.CoroutineContext

class Adapter(context: CoroutineContext, root: File) : CoroutineScope {
    override val coroutineContext = context + Job()

    val birds = FileStore(root, ".json")
        .json(Bird.serializer())
        .inflate(Bird)
        .scope(coroutineContext) {
            birdWatcher().apply { start() }
        }

    private fun Bird.Mutable.birdWatcher(): Watcher =
        watch(watchables) {
            birds.back.put(id, this@birdWatcher)
        }
}
