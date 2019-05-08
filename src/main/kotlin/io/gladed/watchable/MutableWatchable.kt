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

/**
 * A [Watchable] containing a mutable object of type [M], which can both generate and accept changes of type [C].
 */
interface MutableWatchable<M, C : Change> : Watchable<C> {
    /** Remove all items. */
    suspend fun clear()

    /**
     * Suspend until [func] can safely execute on the mutable form [M] of this watchable, returning [func]'s result.
     * [func] must not block or return the mutable form outside of this routine.
     */
    suspend operator fun <U> invoke(func: M.() -> U): U

    /** Return a read-only form of this [MutableWatchable]. */
    fun readOnly(): Watchable<C>

    /**
     * Binds this unbound object to [origin], such that when [origin] changes, this object is updated to match
     * [origin] exactly, until [scope] completes. While bound, this object may not be externally modified or
     * rebound.
     */
    fun bind(scope: CoroutineScope, origin: Watchable<C>): Watcher

    /**
     * Binds this unbound object to [origin], such that for every change to [origin], the change is applied
     * to this object with [apply], until [scope] completes. While bound, this object may not be externally
     * modified or rebound.
     */
    fun <C2 : Change> bind(
        scope: CoroutineScope,
        origin: Watchable<C2>,
        period: Long = IMMEDIATE,
        apply: M.(C2) -> Unit
    ): Watcher

    /** Remove any existing binding for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /**
     * Bind [other] to this so that any change in either object is reflected in the other.
     */
    fun twoWayBind(
        scope: CoroutineScope,
        /** The other side of the bind. The initial state of [other] is delivered to this object. */
        other: MutableWatchable<M, C>
    ): Watcher

    /**
     * Bind [other] to this so that any change in either object is reflected in the other.
     */
    fun <M2, C2 : Change> twoWayBind(
        scope: CoroutineScope,
        /** The other side of the bind. The initial state of [other] is delivered to this object. */
        other: MutableWatchable<M2, C2>,
        /** Update the mutable form of this object with changes from [other]. */
        update: M.(C2) -> Unit,
        /** Update the mutable form of [other] with changes from this object. */
        updateOther: M2.(C) -> Unit
    ): Watcher

    /** The [Watchable] to which this object is bound, if any. */
    val boundTo: Watchable<*>?
}
