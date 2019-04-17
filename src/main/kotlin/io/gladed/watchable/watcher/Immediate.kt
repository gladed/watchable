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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Launch changes into a scope to be processed soon.
 */
@UseExperimental(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
internal class Immediate<C : Change>(
    context: CoroutineContext,
    private val action: suspend (List<C>) -> Unit
) : WatcherBase<C>(context) {

    private val channel = Channel<List<C>>(UNLIMITED)

    private val job = launch {
        channel.consumeEach { action(it) }
    }

    override suspend fun onDispatch(changes: List<C>) {
        channel.send(changes)
    }

    override suspend fun stop() {
        if (!channel.isClosedForSend) channel.close()
        job.join()
        super.stop()
    }
}
