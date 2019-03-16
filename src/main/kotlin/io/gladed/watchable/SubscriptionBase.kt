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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.selects.whileSelect

/** A base implementation of [Subscription] assuming use of [daemon]. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal abstract class SubscriptionBase<C> : Subscription<C>, Channel<List<C>> by Channel<List<C>>(CAPACITY) {
    /**
     * Background processor populating content. It must deliver data from unspecified source(s) into this channel,
     * and gracefully exit when daemonMonitor completes.
     */
    internal abstract val daemon: Job

    /** Close this to signal the daemon to wrap up its work and exit. */
    internal val daemonMonitor = Job()

    override suspend fun join() {
        daemon.join()
    }

    override fun close() {
        // Inform the daemon we are done
        daemonMonitor.cancel()
    }

    override fun batch(
        scope: CoroutineScope,
        periodMillis: Long,
        block: suspend (List<C>) -> Unit
    ): SubscriptionHandle {
        scope.daemon {
            if (periodMillis <= 0) consumeEach { block(it) } else consumeBatches(periodMillis, block)
        }
        return this
    }

    /** Deliver batches of received data no more than once every [periodMillis]. */
    private suspend fun consumeBatches(periodMillis: Long, block: suspend (List<C>) -> Unit) {
        var lastSend = 0L
        val buffer = mutableListOf<C>()

        // Calculate millis between now and when we should send again
        fun remaining() = periodMillis - (System.currentTimeMillis() - lastSend)

        // Supply what we have if appropriate
        suspend fun deliver(): Boolean {
            // Pull down anything already present
            while (true) poll()?.also { buffer += it } ?: break
            if (remaining() <= 0 && buffer.isNotEmpty()) {
                block(buffer.toList())
                lastSend = System.currentTimeMillis()
                buffer.clear()
            }
            return !isClosedForReceive
        }

        deliver()
        whileSelect {
            onReceiveOrNull { received ->
                if (received == null) {
                    lastSend = 0
                    deliver()
                } else {
                    buffer += received
                    deliver()
                }
            }

            daemonMonitor.onJoin {
                lastSend = 0
                deliver()
                false // Always exit if monitor is complete
            }

            val remaining = remaining()
            if (remaining > 0) onTimeout(remaining) {
                deliver()
            }
        }
    }

    companion object {
        const val CAPACITY = 10
    }
}
