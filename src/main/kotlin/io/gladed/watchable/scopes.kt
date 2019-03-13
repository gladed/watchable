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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * For a given receive channel of lists of items, emit combined lists of items no more frequently than every
 * [periodMillis], starting now. If [periodMillis] is non-positive, returns [input] as-is (unbatched).
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
fun <U> CoroutineScope.batch(input: ReceiveChannel<List<U>>, periodMillis: Long = 0): ReceiveChannel<List<U>> =
    if (periodMillis <= 0L) input else produce {
        var lastSend = System.currentTimeMillis()
        val buffer by lazy { mutableListOf<U>() }
        while (true) {
            val received = input.receiveOrNull() ?: break
            delay(periodMillis - (System.currentTimeMillis() - lastSend))

            val toDeliver = if (input.isEmpty) received else {
                // Append all changes into the buffer
                buffer.addAll(received)
                while (!input.isEmpty) {
                    input.poll()?.also { buffer.addAll(it) }
                }
                buffer.toList().also { buffer.clear() }
            }

            lastSend = System.currentTimeMillis()
            send(toDeliver)
        }
    }

/**
 * Similar to [launch] but does not block parent's ability to [Job.join]. Cancels when parent cancels.
 */
fun CoroutineScope.daemon(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job {
    // Launch a new job into the caller's coroutine context, but don't block it up forever.
    val parentJob = coroutineContext[Job]!!
    val daemonScope = CoroutineScope(coroutineContext + SupervisorJob())
    return daemonScope.launch(context, block = block).apply {
        // Cancel this job if parent completes
        parentJob.invokeOnCompletion { cancel() }
    }.apply {
        start()
        invokeOnCompletion {
            daemonScope.coroutineContext.cancel()
        }
    }
}
