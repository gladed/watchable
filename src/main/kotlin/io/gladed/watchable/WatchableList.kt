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

/**
 * A [Watchable] [List] which may also be modified or bound. Use [watchableListOf] to create.
 */
@Suppress("TooManyFunctions")
interface WatchableList<T> : MutableWatchable<MutableList<T>, ListChange<T>>, ReadOnlyWatchableList<T> {

    /** Add [element] to the end of this list, returning true if the list changed. */
    suspend fun add(element: T): Boolean = invoke { add(element) }

    /** Add [elements] to the end of this list, returning true if the list changed. */
    suspend fun addAll(elements: Iterable<T>): Boolean = invoke { addAll(elements) }

    /** Add [elements] to the end of this list, returning true if the list changed. */
    suspend fun addAll(elements: Array<T>): Boolean = invoke { addAll(elements) }

    /** Add [elements] to the end of this list, returning true if the list changed. */
    suspend fun addAll(elements: Sequence<T>): Boolean = invoke { addAll(elements) }

    /** Remove [element] from this list, returning true if the list changed. */
    suspend fun remove(element: T) = invoke { remove(element) }

    /** Remove [elements] from this list, returning true if the list changed. */
    suspend fun removeAll(elements: Iterable<T>): Boolean = invoke { removeAll(elements) }

    /** Remove [elements] from this list, returning true if the list changed. */
    suspend fun removeAll(elements: Array<T>): Boolean = invoke { removeAll(elements) }

    /** Remove [elements] from this list, returning true if the list changed. */
    suspend fun removeAll(elements: Sequence<T>): Boolean = invoke { removeAll(elements) }

    /** Retain only the elements in this list that are found in [elements], returning true if the list changed. */
    suspend fun retainAll(elements: Iterable<T>): Boolean = invoke { retainAll(elements) }

    /** Add [element] to the end of this list. */
    suspend operator fun plusAssign(element: T) { add(element) }

    /** Add [elements] to the end of this list. */
    suspend operator fun plusAssign(elements: Iterable<T>) { addAll(elements) }

    /** Add [elements] to the end of this list. */
    suspend operator fun plusAssign(elements: Array<T>) { addAll(elements) }

    /** Add [elements] to the end of this list. */
    suspend operator fun plusAssign(elements: Sequence<T>) { addAll(elements) }

    /** Remove [element] from this list. */
    suspend operator fun minusAssign(element: T) { remove(element) }

    /** Remove [elements] from this list. */
    suspend operator fun minusAssign(elements: Iterable<T>) { removeAll(elements) }

    /** Remove [elements] from this list. */
    suspend operator fun minusAssign(elements: Array<T>) { removeAll(elements) }

    /** Remove [elements] from this list. */
    suspend operator fun minusAssign(elements: Sequence<T>) { removeAll(elements) }

    /** Return an unmodifiable form of this [WatchableList]. */
    override fun readOnly(): ReadOnlyWatchableList<T>
}
