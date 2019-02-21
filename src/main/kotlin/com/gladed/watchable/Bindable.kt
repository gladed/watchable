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

package com.gladed.watchable

/**
 * An object whose value can be bound to a different object.
 */
interface Bindable<T, C: Change<T>> {
    /** When the value of [other] changes, updates this object. Throws if already bound. */
    fun bind(other: Watchable<T, C>)

    /** Cancel any existing binding that exists for this object. */
    fun unbind()

    /** Return true if this object is already bound. */
    fun isBound() = boundTo != null

    /** The current binding, if any. */
    val boundTo: Watchable<T, C>?
}
