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

/**
 * A [Watchable] containing a mutable object of type [M], which can both generate and accept changes of type [C].
 */
interface MutableWatchable<M, C : Change> : Watchable<C> {
    /**
     * Remove all items.
     */
    suspend fun clear()

    /**
     * Suspend until [func] can safely execute, reading and/or writing data on [M] as desired and returning
     * the result. Note: if currently bound ([isBound] returns true), attempts to modify [M] will throw.
     */
    suspend fun <U> use(
        /**
         * A function to inspect and/or return data from the mutable form of this object. Must not block
         * or return the mutable form outside of this routine.
         */
        func: M.() -> U
    ): U

    /**
     * Binds this unbound object to [origin], such that when [origin] changes, this object is updated to match
     * [origin] exactly, until [scope] completes. While bound, this object may not be externally modified or
     * rebound.
     */
    suspend fun bind(scope: CoroutineScope, origin: Watchable<C>)

    /**
     * Binds this unbound object to [origin], such that for every change to [origin], the change is applied
     * to this object with [apply], until [scope] completes. While bound, this object may not be externally
     * modified or rebound.
     */
    suspend fun <C2 : Change> bind(
        scope: CoroutineScope,
        origin: Watchable<C2>,
        apply: M.(C2) -> Unit
    )

    /** Remove any existing binding for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /** The [Watchable] to which this object is bound, if any. */
    val boundTo: Watchable<*>?
}
