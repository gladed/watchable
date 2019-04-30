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

/**
 * Represents an object being held for use with resources or side effects to be managed by
 * this object.
 */
interface Hold {
    /** Handle the initial creation of the underlying held object. */
    suspend fun onCreate()

    /** Start any internals with a delayed start. */
    suspend fun onStart()

    /** Called to gently stop any internal aspects of the hold. */
    suspend fun onStop()

    /** Immediately cancel the hold. */
    fun onCancel()

    /** Handle impending removal of the held object. */
    suspend fun onRemove()

    companion object {

        /** Create a [Hold] object with supplied implementations. */
        operator fun invoke(
            onStart: suspend () -> Unit = { },
            onStop: suspend () -> Unit = { },
            onCancel: () -> Unit = { },
            onRemove: suspend () -> Unit = { },
            onCreate: suspend () -> Unit = { }
        ): Hold = object : Hold {
            override suspend fun onStart() { onStart() }
            override suspend fun onStop() { onStop() }
            override fun onCancel() { onCancel() }
            override suspend fun onRemove() { onRemove() }
            override suspend fun onCreate() { onCreate() }
        }
    }
}

/** Combine the behaviors of this [Hold] object with [other]. */
operator fun Hold.plus(other: Hold) = Hold(
    onStart = { onStart(); other.onStart() },
    onStop = { onStop(); other.onStop() },
    onCancel = { onCancel(); other.onCancel() },
    onRemove = { onRemove(); other.onRemove() },
    onCreate = { onCreate(); other.onCreate() }
)

/** Combine the behaviors of this [Hold] object with a [Watcher]. */
operator fun Hold.plus(other: Watcher) = Hold(
    onStart = { onStart(); other.start() },
    onStop = { onStop(); other.stop() },
    onCancel = { onCancel(); other.cancel() },
    onRemove = { onRemove() },
    onCreate = { onCreate() }
)
