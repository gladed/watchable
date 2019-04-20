package logic

import io.gladed.watchable.Watcher
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import model.Bird
import model.MutableBird
import store.Store
import store.scope
import kotlin.coroutines.CoroutineContext

/** Implement business logic required by components of the application. */
open class Logic(
    context: CoroutineContext,
    birdStore: Store<Bird>
) : CoroutineScope {
    final override val coroutineContext = context + Job()

    val birds = birdStore
        .inflate(MutableBird)
        .scope(coroutineContext) { birdWatcher().apply { start() }
    }

    private fun MutableBird.birdWatcher(): Watcher =
        watch(watchables) {
            if (!it.isInitial) {
                // For any non-initial change, store.
                birds.back.put(id, this@birdWatcher)
            }
        }
}