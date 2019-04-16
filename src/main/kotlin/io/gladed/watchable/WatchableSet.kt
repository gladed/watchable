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

/** A [Watchable] [Set] which may also be modified or bound. Use [watchableSetOf] to create. */
@Suppress("TooManyFunctions")
interface WatchableSet<T> : MutableWatchable<MutableSet<T>, SetChange<T>>, ReadOnlyWatchableSet<T> {

    /** Add [element] to this set, returning true if the set changed. */
    suspend fun add(element: T): Boolean = use { add(element) }

    /** Add [elements] to this set, returning true if the set changed. */
    suspend fun addAll(elements: Collection<T>): Boolean = use { addAll(elements) }

    /** Add [elements] to this set, returning true if the set changed. */
    suspend fun addAll(elements: Iterable<T>): Boolean = use { addAll(elements) }

    /** Add [elements] to this set, returning true if the set changed. */
    suspend fun addAll(elements: Array<T>): Boolean = use { addAll(elements) }

    /** Add [elements] to this set, returning true if the set changed. */
    suspend fun addAll(elements: Sequence<T>): Boolean = use { addAll(elements) }

    /** Remove [element] from this set, returning true if the set changed. */
    suspend fun remove(element: T) = use { remove(element) }

    /** Remove [elements] from the set, returning true if the set changed. */
    suspend fun removeAll(elements: Iterable<T>): Boolean = use { removeAll(elements) }

    /** Remove [elements] from the set, returning true if the set changed. */
    suspend fun removeAll(elements: Array<T>): Boolean = use { removeAll(elements) }

    /** Remove [elements] from the set, returning true if the set changed. */
    suspend fun removeAll(elements: Sequence<T>): Boolean = use { removeAll(elements) }

    /** Retain only the elements in this set that are found in [elements], returning true if the set changed. */
    suspend fun retainAll(elements: Iterable<T>): Boolean = use { retainAll(elements) }

    /** Add [value] to this set. */
    suspend operator fun plusAssign(value: T) { add(value) }

    /** Add [elements] to this set. */
    suspend operator fun plusAssign(elements: Array<T>) { addAll(elements) }

    /** Add [elements] to this set. */
    suspend operator fun plusAssign(elements: Iterable<T>) { addAll(elements) }

    /** Add [elements] to this set. */
    suspend operator fun plusAssign(elements: Sequence<T>) { addAll(elements) }

    /** Remove [element] from this set. */
    suspend operator fun minusAssign(element: T) { remove(element) }

    /** Remove [elements] from this set. */
    suspend operator fun minusAssign(elements: Array<T>) { removeAll(elements) }

    /** Remove [elements] from this set. */
    suspend operator fun minusAssign(elements: Iterable<T>) { removeAll(elements) }

    /** Remove [elements] from this set. */
    suspend operator fun minusAssign(elements: Sequence<T>) { removeAll(elements) }

    /** Return an unmodifiable form of this [WatchableSet]. */
    override fun readOnly(): ReadOnlyWatchableSet<T>
}
