/*
 * (c) Copyright 2019 Glade Diviney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gladed.watchable.watcher

import io.gladed.watchable.Change
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onReceiveOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Buffers changes and shares them out every so often.
 */
internal class Periodic<C : Change>(
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
            @Suppress("ControlFlowWithEmptyBody", "EmptyWhileBlock") // Infinte loop until handleChanges returns false
            while (handleChanges()) { }
        }
    }

    /** Return the current system time. */
    private fun now() = System.currentTimeMillis()

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun handleChanges(): Boolean = select {
        val clause = changeChannel.onReceiveOrNull()
        clause { received ->
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

    override suspend fun stop() {
        if (isActive) withContext(coroutineContext) {
            changeChannel.close()
        }
        super.stop()
    }
}
