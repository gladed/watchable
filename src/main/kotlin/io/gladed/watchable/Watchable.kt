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
 * Wraps type [T] so that it may be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> : CoroutineScope {

    /** Return the current value of [T]. */
    suspend fun get(): T

    /**
     * On this [CoroutineScope], deliver every change on this [Watchable] to [func], starting with its initial
     * state. Changes will stop arriving when this scope completes, when the [Watchable]'s scope completes, when
     * the returned [Job] is cancelled, or if [func] throws.
     */
    fun CoroutineScope.watch(
        func: (C) -> Unit
    ) =
        watchBatches { changes ->
            for (change in changes) {
                if (coroutineContext.isActive) func(change) else break
            }
        }

    /**
     * On this [CoroutineScope], deliver lists changes on this [Watchable] to [func], starting with its initial
     * state. Changes will stop arriving when this scope completes, when the [Watchable]'s scope completes, when
     * the returned [Job] is cancelled, or if [func] throws.
     */
    fun CoroutineScope.watchBatches(
        func: suspend (List<C>) -> Unit
    ): Job
}

/**
 * On this [CoroutineScope], deliver changes on [watchable] to [func], starting with its initial
 * state. Changes will stop arriving when this scope completes, when the [watchable]'s scope completes, when
 * the returned [Job] is cancelled, or if [func] throws.
 */
fun <T, C : Change<T>> CoroutineScope.watch(watchable: Watchable<T, C>, func: (C) -> Unit): Job =
    with(watchable) { this@watch.watch(func) }

/**
 * On this [CoroutineScope], deliver lists of changes on [watchable] to [func], starting with its initial
 * state. Changes will stop arriving when this scope completes, when the [watchable]'s scope completes, when
 * the returned [Job] is cancelled, or if [func] throws.
 */
fun <T, C : Change<T>> CoroutineScope.watchBatches(watchable: Watchable<T, C>, func: suspend (List<C>) -> Unit) =
    with(watchable) { this@watchBatches.watchBatches(func) }
