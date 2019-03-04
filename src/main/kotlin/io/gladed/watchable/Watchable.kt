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

/**
 * An object wrapping type [T] which can be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> : CoroutineScope {

    /** Return the current value of [T]. */
    suspend fun get(): T

    /**
     * Receive lists of changes in [func] for all changes to this [Watchable] (starting with its initial state)
     * until this [Watchable]'s scope completes OR [scope] completes OR the returned [Job] is cancelled.
     */
    fun watchBatches(
        /** Scope to use while watching. */
        scope: CoroutineScope,
        /** The block to invoke within this [scope] whenever a change occurs. */
        func: suspend (List<C>) -> Unit
    ): Job

    /**
     * Receive individual changes in [func] for all changes to this active [Watchable] (starting with its initial state)
     * until this [Watchable]'s scope completes OR [scope] completes OR the returned [Job] is cancelled.
     */
    fun watch(
        scope: CoroutineScope,
        /** The block to invoke within this [CoroutineScope] whenever a change occurs. */
        func: (C) -> Unit
    ): Job =
        watchBatches(scope) { changes ->
            for (change in changes) {
                if (coroutineContext.isActive) func(change) else break
            }
        }
}

fun <T, C : Change<T>> CoroutineScope.watch(watchable: Watchable<T, C>, func: (C) -> Unit): Job =
    watchable.watch(this, func)

fun <T, C : Change<T>> CoroutineScope.watchBatches(watchable: Watchable<T, C>, func: suspend (List<C>) -> Unit): Job =
    watchable.watchBatches(this, func)
