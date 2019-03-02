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
 * An object whose contents are updated automatically when another object changes.
 */
interface Bindable<T, C : Change<T>> : CoroutineScope {
    /**
     * Binds this unbound object so that when [other] changes, it is updated accordingly. This object must not be
     * modified while bound.
     */
    fun bind(scope: CoroutineScope, other: Watchable<T, C>)

    /** Cancel any existing binding that exists for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /** The current binding, if any. */
    val boundTo: Watchable<*, *>?
}

fun <T, C : Change<T>> CoroutineScope.bind(source: Watchable<T, C>, dest: Bindable<T, C>) {
    dest.bind(this, source)
}
