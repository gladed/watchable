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

package io.gladed.watchable.store

import io.gladed.watchable.util.guard
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * A deferred attempt to acquire an instance of [T] on behalf of one or more holding entities.
 * When the list of holding entities becomes empty, the hold is stopped.
 */
internal class MultiHold<E, T : Any>(val hold: Deferred<Pair<T, Hold>>) {

    /** Construct a non-deferred version of this [MultiHold]. */
    constructor(value: T, hold: Hold) : this(CompletableDeferred(value to hold))

    private val entities = mutableSetOf<E>().guard()

    /** Add [entity] to the collection of reserved entities. */
    suspend fun reserve(entity: E) {
        entities { add(entity) }
    }

    /**
     * Release a hold on behalf of the first [E] matching test and return true if the object can be discarded
     * (e.g. no more entities).
     */
    suspend fun release(test: (E) -> Boolean): Boolean =
        entities { find(test) }?.let { release(it) } ?: false

    /** Removes a holding entity and returns true if this object can be discarded (e.g. no more entities). */
    suspend fun release(entity: E) =
        if (entities { remove(entity); isEmpty() }) {
            stop()
            true
        } else {
            false
        }

    /** Stop holding as we delete this item. */
    suspend fun remove() {
        val toStop = hold.await().second
        toStop.onRemove()
        toStop.onStop()
    }

    /** Stop holding regardless of the number of holding entities. */
    suspend fun stop() {
        // If the request isn't done then cancel it
        hold.cancel()
        @Suppress("EmptyCatchBlock")
        try {
            // Stop watching if not already cancelled
            if (!hold.isCancelled) {
                hold.await().second.onStop()
            }
        } catch (c: CancellationException) {
            // Cancellations don't matter, keep stopping
        }
    }
}
