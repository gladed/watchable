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

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

/** Exposes a [deferred] as a [Watcher]. */
@OptIn(ExperimentalCoroutinesApi::class)
internal open class DeferredWatcher(private val deferred: Deferred<Watcher>) : Watcher {
    override fun cancel() {
        if (deferred.isCancelled) return
        deferred.cancel()
        if (deferred.isCompleted && deferred.getCompletionExceptionOrNull() == null) {
            deferred.getCompleted().cancel()
        }
    }

    override suspend fun stop() {
        if (deferred.isCancelled) return
        deferred.await().stop()
    }

    override suspend fun start() {
        if (deferred.isCancelled) return
        deferred.await().start()
    }
}
