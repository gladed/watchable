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

package io.gladed.watchable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/**
 * Perform some work in the background until the returned handle is closed or cancelled.
 */
internal fun CoroutineScope.operate(
    /** Work to perform, stopping as soon as the supplied Mutex can be locked. */
    work: suspend CoroutineScope.(Mutex) -> Unit
): WatchHandle {

    // May be unlocked to allow [work] to shut down gently.
    val mutex = Mutex(locked = true)

    // Uses supervisor to allow the parent scope to complete without waiting
    val operationScope = CoroutineScope(coroutineContext + SupervisorJob())
    val operation = operationScope.launch {
        work(mutex)
    }

    // When the current scope completes, cancel the operation
    coroutineContext[Job]?.invokeOnCompletion { operation.cancel() }

    return object : WatchHandle {
        override fun cancel() { operation.cancel() }
        override suspend fun join() { operation.join() }
        override fun close() {
            if (mutex.isLocked) mutex.unlock()
        }
    }
}
