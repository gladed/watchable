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

import io.gladed.watchable.Watcher

/** DSL for creating a [Hold]. */
class HoldBuilder {
    private var onCreates: (suspend () -> Unit)? = null
    private var onStarts: (suspend () -> Unit)? = null
    private var onStops: (suspend () -> Unit)? = null
    private var onCancels: (() -> Unit)? = null
    private var onRemoves: (suspend () -> Unit)? = null

    /** Provide a function to be invoked when an object is being created. */
    fun onCreate(func: suspend () -> Unit) {
        onCreates = onCreates?.let { suspend { it(); func() } } ?: func
    }

    /** Starts operations related to the hold. */
    fun onStart(func: suspend () -> Unit) {
        onStarts = onStarts?.let { suspend { it(); func() } } ?: func
    }

    /** Invoke when the hold is to be stopped. */
    fun onStop(func: suspend () -> Unit) {
        onStops = onStops?.let { suspend { it(); func() } } ?: func
    }

    /** Cancels the hold immediately with no further handling. */
    fun onCancel(func: () -> Unit) {
        onCancels = onCancels?.let { { it(); func() } } ?: func
    }

    /** Invoke when the held object is to be removed. */
    fun onRemove(func: suspend () -> Unit) {
        onRemoves = onRemoves?.let { suspend { it(); func() } } ?: func
    }

    /** Adopt a [Watcher] as part of the [Hold] to be built, cancelling, stopping, and starting it together. */
    fun onWatcher(watcher: Watcher) {
        onCancel { watcher.cancel() }
        onStop { watcher.stop() }
        onStart { watcher.start() }
    }

    /** Build the [Hold] from the current builder state. */
    fun build() =
        Hold(onCreate = onCreates ?: { },
            onStart = onStarts ?: { },
            onStop = onStops ?: { },
            onCancel = onCancels ?: { },
            onRemove = onRemoves ?: { })
}