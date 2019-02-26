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

/** Describes a change to a [List]. */
sealed class ListChange<T> : Change<List<T>> {
    /** The initial state of the list at the time watching began. */
    data class Initial<T>(val initial: List<T>) : ListChange<T>()

    /** An addition of an element to the list. */
    data class Add<T>(val index: Int, val added: T) : ListChange<T>()

    /** A removal of an element in the list. */
    data class Remove<T>(val index: Int, val removed: T) : ListChange<T>()

    /** A replacement of the element at a specific place in the list. */
    data class Replace<T>(val index: Int, val removed: T, val added: T) : ListChange<T>()
}
