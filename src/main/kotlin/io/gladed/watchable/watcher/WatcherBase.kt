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
import io.gladed.watchable.Watcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

/** Parent for all types of dispatchers. */
internal abstract class WatcherBase<C : Change>(
    val context: CoroutineContext
) : Watcher, CoroutineScope {
    override val coroutineContext = context + Job()

    abstract suspend fun onDispatch(changes: List<C>)

    /** Deliver this change to the watcher according to its timing rules. */
    suspend fun dispatch(changes: List<C>) =
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
