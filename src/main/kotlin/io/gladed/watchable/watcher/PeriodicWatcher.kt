package io.gladed.watchable.watcher

import io.gladed.watchable.Change
import io.gladed.watchable.TestContextWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * A [Watcher] that buffers changes and shares them out every so often.
 */
@UseExperimental(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
internal class PeriodicWatcher<C : Change>(
    context: CoroutineContext,
    /** A non-zero period between change announcements. */
    private val period: Long,
    /** Function to invoke when changes are ready. */
    private val action: suspend (List<C>) -> Unit
) : WatcherBase<C>(context) {

    private val changeChannel = Channel<List<C>>(Channel.UNLIMITED)
    private val outstanding = mutableListOf<C>()
    private var next = now() + period

    init {
        launch {
            @Suppress("EmptyWhileBlock") // Intended
            while (handleChanges()) { }
        }
    }

    /** Return the current system time (or test time if in unit test). */
    private fun now(): Long =
        context[TestContextWrapper]?.testContext?.now() ?: System.currentTimeMillis()

    private suspend fun handleChanges(): Boolean = select {
        changeChannel.onReceiveOrNull { received ->
            if (received == null) {
                action(outstanding)
                false
            } else {
                outstanding += received
                true
            }
        }

        if (outstanding.isNotEmpty()) {
            onTimeout(next - now()) {
                action(outstanding.toList())
                outstanding.clear()
                next = System.currentTimeMillis() + period
                true
            }
        }
    }

    override suspend fun onDispatch(changes: List<C>) {
        changeChannel.send(changes)
    }

    override suspend fun close() {
        if (isActive) withContext(coroutineContext) {
            changeChannel.close()
        }
        super.close()
    }
}
