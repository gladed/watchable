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

/** Represents an object being held for use. */
interface Hold {
    /** Return when the watcher has become effective. */
    suspend fun start()

    /** Gracefully stop, suspending as necessary to allow outstanding operations to complete. */
    suspend fun stop()

    /** Immediately stop. */
    fun cancel()

    /** Action to take upon removing this item. */
    suspend fun remove()

    operator fun plus(other: Hold) = Hold(
        onStart = { start(); other.start() },
        onStop = { stop(); other.stop() },
        onCancel = { cancel(); other.cancel() },
        onRemove = { remove(); other.remove() }
    )

    operator fun plus(other: Watcher) = this + Hold(
        onStart = { other.start() },
        onStop = { other.stop() },
        onCancel = { other.cancel() }
    )

    companion object {
        operator fun invoke(
            onStart: suspend () -> Unit = { },
            onStop: suspend () -> Unit = { },
            onCancel: () -> Unit = { },
            onRemove: suspend () -> Unit = { }
        ) = object : Hold {
            override suspend fun start() { onStart() }
            override suspend fun stop() { onStop() }
            override fun cancel() { onCancel() }
            override suspend fun remove() { onRemove() }
        }
    }
}
