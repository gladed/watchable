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
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ClosedReceiveChannelException
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
fun <U> CoroutineScope.batch(input: ReceiveChannel<List<U>>, periodMillis: Long): ReceiveChannel<List<U>> =
    if (periodMillis <= 0L) input else produce {
        try {
            var lastSend = System.currentTimeMillis()
            val buffer = mutableListOf<U>()
            while (true) {
                val received = input.receive()
                val toDelay = periodMillis - (System.currentTimeMillis() - lastSend)
                val toDeliver: List<U> = if (toDelay <= 0) received else {
                    // Delay until min period is reached
                    delay(toDelay)
                    if (input.isEmpty) {
                        // No further changes so send the original list as-is
                        received
                    } else {
                        // Other changes, so combine into temporary buffer
                        buffer.addAll(received)
                        while (!input.isEmpty) {
                            buffer.addAll(input.poll()!!)
                        }
                        buffer.toList().also { buffer.clear() }
                    }
                }
                lastSend = System.currentTimeMillis()
                send(toDeliver)
            }
        } catch (e: ClosedReceiveChannelException) { } // Ignore
    }

/**
 * Launch a new coroutine without blocking the current thread, returning a reference to a Job.
 * The coroutine is cancelled when the returned [Job] or the calling [CoroutineScope] is cancelled.
 * The resulting job is automatically cancelled when the parent completes.
 */
fun CoroutineScope.daemon(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job =
    // Launch a new job into the caller's coroutine context, but don't block it up forever.
    CoroutineScope(coroutineContext + SupervisorJob()).launch(context, start, block).apply {
        this@daemon.coroutineContext[Job]!!.invokeOnCompletion {
            cancel()
        }
    }
