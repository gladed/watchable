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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.newSingleThreadContext

/**
 * An object wrapping type [T] which can be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> : CoroutineScope {
    /**
     * Receive lists of changes in [block] for all changes to the [watchable] (starting with its initial state) until
     * the completion of this [Watchable]'s context, this [CoroutineScope], or the returned [Job] is cancelled.
     */
    fun CoroutineScope.watchBatches(
        /** The block to invoke within this [CoroutineScope] whenever a change occurs. */
        block: (List<C>) -> Unit
    ): Job

    /**
     * Receive individual changes in [block] for all changes to the [watchable] (starting with its initial state)
     * until the completion of this [Watchable]'s context, this [CoroutineScope], or the returned [Job] is cancelled.
     */
    fun CoroutineScope.watch(
        /** The block to invoke within this [CoroutineScope] whenever a change occurs. */
        block: (C) -> Unit
    ): Job =
        watchBatches { changes ->
            for (change in changes) {
                if (coroutineContext.isActive) block(change) else break
            }
        }

    /** Return true if this [Watchable]'s scope is still active, allowing new [watch] requests to succeed. */
    fun isActive() = coroutineContext.isActive

    companion object {
        @UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class)
        internal val singleDispatcher = newSingleThreadContext("watchable")
    }
}
