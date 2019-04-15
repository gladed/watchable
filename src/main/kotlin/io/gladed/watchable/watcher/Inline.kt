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
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Respond to changes while they are being made.
 */
internal class Inline<C : Change>(
    context: CoroutineContext,
    private val action: suspend (List<C>) -> Unit
) : WatcherBase<C>(context) {

    override suspend fun onDispatch(changes: List<C>) {
        withContext(coroutineContext) {
            @Suppress("TooGenericExceptionCaught") // Necessary to propagate ANY exception
            try {
                action(changes)
                null
            } catch (t: Throwable) {
                t
            }
        }?.also {
            // Re-throw on the caller's context
            throw it
        }
    }
}
