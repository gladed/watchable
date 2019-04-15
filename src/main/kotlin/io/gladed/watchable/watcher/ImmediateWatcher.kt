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
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

/**
 * Launch changes into a scope to be processed soon.
 */
internal class ImmediateWatcher<C : Change>(
    context: CoroutineContext,
    private val action: suspend (List<C>) -> Unit
) : WatcherBase<C>(context) {

    private val inOrder = Mutex()

    override suspend fun onDispatch(changes: List<C>) {
        // In the background, wait for order lock and run action within it to prevent overlapping calls
        launch(context) {
            inOrder.withLock { action(changes) }
        }
    }
}
