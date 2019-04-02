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
import kotlinx.coroutines.isActive

/**
 * Wraps container [T] (having objects of type [V])  so that it may be watched for
 * changes (of type [C]).
 *
 * Each watch operation takes a [CoroutineScope]. Callbacks are delivered using this scope's context, and stop
 * automatically when this scope cancels or completes.
 *
 * Each watch operation also returns a [WatchHandle] which may be used to independently cancel or join the watch
 * operation.
 */
interface Watchable<out T, out V, out C : Change> {

    /** Return an immutable copy of the current value of [T]. */
    val value: T

    /**
     * Deliver all changes from this [Watchable] to [func] as lists of [Change] objects until [scope] completes.
     */
    fun batch(
        /** Scope within which to process changes. */
        scope: CoroutineScope,
        /** The minimum period between lists of change events. */
        minPeriod: Long = 0,
        /** Function which processes elements as they arrive. */
        func: suspend (List<C>) -> Unit
    ): WatchHandle

    /**
     * Deliver all changes from this [Watchable] to [func] as individual [Change] objects until [scope] completes.
     */
    fun watch(scope: CoroutineScope, func: suspend (C) -> Unit): WatchHandle =
        batch(scope) { changes ->
            for (change in changes) {
                if (scope.isActive) func(change) else break
            }
        }
}
