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

/** A base implementation of [Subscription] assuming use of [daemon]. */
internal abstract class SubscriptionBase<C> : Subscription<C> {
    protected abstract val daemon: Job

    protected val channel = Channel<List<C>>(CAPACITY)

    override val receiver get() = channel

    override suspend fun join() { daemon.join() }

    override fun close() { channel.close() }

    override fun cancel() { channel.cancel() }

    companion object {
        const val CAPACITY = 10
    }
}