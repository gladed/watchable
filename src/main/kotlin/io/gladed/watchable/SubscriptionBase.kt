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
import kotlinx.coroutines.sync.Mutex

/** Create a subscription out of supplied work. */
internal fun CoroutineScope.subscription(
    /** Work to perform, stopping as soon as the supplied Mutex can be locked. */
    work: suspend CoroutineScope.(Mutex) -> Unit
): SubscriptionHandle =
    object : SubscriptionHandle {
        /** Closed to signal the daemon should finish outstanding work and exit. */
        protected val daemonClose = Mutex(locked = true)

        val daemon: Job = daemon {
            work(daemonClose)
        }.also {
            coroutineContext[Job]?.invokeOnCompletion { cancel() }
        }

        override fun cancel() { daemon.cancel() }
        override suspend fun join() { daemon.join() }
        override fun close() { daemonClose.unlock() }
    }
