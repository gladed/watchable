package io.gladed.watchable.watcher

import io.gladed.watchable.Change
import io.gladed.watchable.Busy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

internal abstract class WatcherBase<C : Change>(
    val context: CoroutineContext
) : Watcher<C>, Busy, CoroutineScope {
    override val coroutineContext = context + Job()

    abstract suspend fun onDispatch(changes: List<C>)

    override suspend fun dispatch(changes: List<C>) =
        if (context.isActive && coroutineContext.isActive) {
            onDispatch(changes)
            true
        } else {
            false
        }

    override fun cancel() {
        coroutineContext[Job]?.cancel()
    }

    override suspend fun close() {
        // Allow all current children to complete
        coroutineContext[Job]?.children?.forEach { it.join() }
        // Cancel now and wait for completion
        coroutineContext[Job]?.cancelAndJoin()
    }
}
