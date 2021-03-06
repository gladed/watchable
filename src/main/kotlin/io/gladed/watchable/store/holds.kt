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

/** Combine the behaviors of this [Hold] object with [other]. */
operator fun Hold.plus(other: Hold) = Hold(
    onStart = { onStart(); other.onStart() },
    onStop = { onStop(); other.onStop() },
    onCancel = { onCancel(); other.onCancel() },
    onRemove = { onRemove(); other.onRemove() },
    onCreate = { onCreate(); other.onCreate() }
)

/** Combine the behaviors of this [Hold] object with a [Watcher]. */
operator fun Hold.plus(other: Watcher) = this + other.toHold()

/** Return a [Hold] form of this [Watcher]. */
fun Watcher.toHold() = Hold(
    onCancel = { cancel() },
    onStart = { start() },
    onStop = { stop() }
)
