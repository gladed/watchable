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
 * A [Watchable] wrapping type [T] which may also be mutated in the form of an [M], and bound to other [Watchable]
 * sources.
 */
interface MutableWatchable<T, M : T, C : Change<T>> : Watchable<T, C> {
    /**
     * Suspend until [func] can safely execute, reading and/or writing data on [M] as desired and returning
     * the result. Note: if currently bound ([isBound] returns true), attempts to modify [M] will throw.
     */
    suspend fun <U> use(func: M.() -> U): U

    /**
     * Completely replace the contents of this watchable.
     */
    suspend fun set(value: T)

    /**
     * Binds this unbound object to [source], such that when [source] changes, this object is updated to match
     * [source] exactly. This object may not be modified while bound. When this object's [CoroutineScope] completes,
     * no further binding related changes are applied. Bindings may not be circular.
     */
    fun bind(source: Watchable<T, C>)

    /**
     * Binds this unbound object to [source], such that for every change to [source], the mutable form of this
     * object is updated with [apply]. This object may not be otherwise modified while bound. When this object's
     * [CoroutineScope] completes, apply is no longer invoked. Bindings may not be circular.
     */
    fun <T2, C2 : Change<T2>> bind(source: Watchable<T2, C2>, apply: M.(C2) -> Unit)

    /** Cancel any existing binding that exists for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /** The [Watchable] to which this object is bound, if any. */
    val boundTo: Watchable<*, *>?
}
