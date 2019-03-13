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
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.isActive
import java.time.Duration

/**
 * Wraps type [T] so that it may be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> {

    /** Return the current value of [T]. */
    suspend fun get(): T

    /**
     * Return a channel which will receive successive lists of changes as they occur.
     */
    fun subscribe(scope: CoroutineScope): ReceiveChannel<List<C>>

    /**
     * Deliver changes for this [Watchable] to [func], starting with its initial state, until
     * the returned job is cancelled or the [scope] completes.
     */
    fun watch(scope: CoroutineScope, func: (C) -> Unit) =
        watchBatches(scope, Duration.ZERO) { changes ->
            for (change in changes) {
                if (scope.isActive) func(change) else break
            }
        }

    /**
     * Deliver lists of changes for this [Watchable] to [func], starting with its initial state, until
     * the returned job is cancelled or the [scope] completes.
     */
    fun watchBatches(
        scope: CoroutineScope,
        /** The minimum time between change notifications, or [Duration.ZERO] (the default) for no delay. */
        minPeriod: Duration = Duration.ZERO,
        func: suspend (List<C>) -> Unit
    ): Job
}
