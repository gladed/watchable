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

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/** A base implementation of [Subscription] assuming use of [daemon]. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal abstract class SubscriptionBase<C> : Subscription<C>, Channel<List<C>> by Channel<List<C>>(CAPACITY) {

    /**
     * Background processor for handling subscription. It must deliver data (from an unspecified source) and
     * gracefully exit if daemonMonitor completes.
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

    companion object {
        const val CAPACITY = 10
        /**
         * A utility method for efficiently combining outstanding changes from a given receive channel into a
         * single change list for delivery.
         */
        fun <C> compile(changes: List<C>, rx: ReceiveChannel<List<C>>): List<C> {
            var compiled: MutableList<C>? = null
            // Drain out additional events if any
            while (true) rx.poll()?.also {
                if (compiled == null) {
                    compiled = changes.toMutableList()
                }
                compiled!!.addAll(it)
            } ?: break
            return compiled ?: changes
        }
    }
}
