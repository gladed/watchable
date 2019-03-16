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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

/**
 * Similar to [launch] but does not block parent's ability to [Job.join]. Cancels when parent cancels.
 */
internal fun CoroutineScope.daemon(
    block: suspend CoroutineScope.() -> Unit
): Job {
    // Launch a new job into the caller's coroutine context, but don't block it up forever.
    val parentJob = coroutineContext[Job]!!
    val daemonScope = CoroutineScope(coroutineContext + SupervisorJob())
    return daemonScope.launch(block = block).apply {
        // Cancel this job if parent completes
        parentJob.invokeOnCompletion { cancel() }
        invokeOnCompletion {
            // Close the daemon if job is cancelled
            daemonScope.coroutineContext.cancel()
        }
        start()
    }
}

/**
 * Return a list of items include [received] and any outstanding items already received on this channel.
 */
internal fun <C> ReceiveChannel<List<C>>.appendPolled(received: List<C>): List<C> {
    var compiled: MutableList<C>? = null

    // Drain out additional events if any
    while (true) poll()?.also {
        if (compiled == null) {
            compiled = received.toMutableList()
        }
        compiled!!.addAll(it)
    } ?: break

    return compiled ?: received
}
