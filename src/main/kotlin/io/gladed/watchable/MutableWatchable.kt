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
 * A [Watchable] which may be mutated in the form of an [M] and bound to other [Watchable] sources.
 */
interface MutableWatchable<T, M : T, C : Change<T>> : Watchable<T, C> {
    /**
     * Suspend until [func] can safely execute, reading and/or writing data on [M] as desired and returning
     * the result. Note: if currently bound ([isBound] returns true), attempts to modify [M] will throw.
     */
    suspend fun <U> use(
        /** A function to quickly inspect and/or return data on the mutable form of this object. Do not block,
         * [use] other [MutableWatchable] objects, or return the mutable form outside of this routine. */
        func: M.() -> U
    ): U

    /**
     * Completely replace the contents of this watchable.
     */
    suspend fun set(value: T)

    /**
     * Binds this unbound object to [origin], such that when [origin] changes, this object is updated to match
     * [origin] exactly, until [scope] completes. While bound, this object may not be externally modified or
     * rebound.
     */
    fun bind(scope: CoroutineScope, origin: Watchable<T, C>)

    /**
     * Binds this unbound object to [origin], such that for every change to [origin], the change is applied
     * to this object with [apply], until [scope] completes. While bound, this object may not be externally
     * modified or rebound.
     */
    fun <T2, C2 : Change<T2>> bind(scope: CoroutineScope, origin: Watchable<T2, C2>, apply: M.(C2) -> Unit)

    /** Cancel any existing binding that exists for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /** The [Watchable] to which this object is bound, if any. */
    val boundTo: Watchable<*, *>?
}
