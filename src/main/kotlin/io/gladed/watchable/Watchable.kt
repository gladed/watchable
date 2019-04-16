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

import io.gladed.watchable.Period.IMMEDIATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex

/**
 * An object that allows you to watch for changes of type [C].
 *
 * Each watch operation takes a [CoroutineScope]. Callbacks are delivered using this scope's context, and stop
 * automatically when this scope cancels or completes.
 *
 * Each watch operation also returns a [Watcher] which may be used to independently cancel or join the watch
 * operation.
 */
interface Watchable<out C : Change> {

    /**
     * Deliver all changes from this [Watchable] to [func] as lists of [Change] objects until [scope] completes.
     */
    suspend fun batch(
        /** Scope within which to process changes. */
        scope: CoroutineScope,
        /** When to receive changes, see [Period]. */
        period: Long = IMMEDIATE,
        /** Function which processes elements as they arrive. */
        func: suspend (List<C>) -> Unit
    ): Watcher

    /**
     * Deliver all changes from this [Watchable] to [func] as individual [Change] objects until [scope] completes.
     */
    suspend fun watch(
        scope: CoroutineScope,
        /** When to receive changes, see [Period]. */
        period: Long = IMMEDIATE,
        func: suspend (C) -> Unit
    ): Watcher =
        batch(scope, period) { changes ->
            for (change in changes) {
                if (scope.isActive) func(change) else break
            }
        }

    /** Suspend, calling [func] as changes arrive, and return when [func] returns true. */
    suspend fun waitFor(scope: CoroutineScope, func: () -> Boolean) {
        if (func()) return
        val mutex = Mutex(locked = true)
        val handle = scope.batch(this) {
            if (mutex.isLocked && func()) mutex.unlock()
        }
        mutex.lock() // Suspend until success
        handle.cancel() // Cancel listening
    }
}
