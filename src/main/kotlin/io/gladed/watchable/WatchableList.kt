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
 * A [Watchable] wrapper for a [List] which may also be modified or bound. Use [watchableListOf] to create.
 */
@Suppress("TooManyFunctions")
class WatchableList<T> internal constructor(
    initial: Collection<T>
) : MutableWatchableBase<List<T>, T, MutableList<T>, ListChange<T>>(), ReadOnlyWatchableList<T> {
    override var immutable: List<T> = initial.toList()

    override val size: Int get() = immutable.size
    override fun contains(element: T) = immutable.contains(element)
    override fun containsAll(elements: Collection<T>) = immutable.containsAll(elements)
    override fun get(index: Int) = immutable[index]
    override fun indexOf(element: T) = immutable.indexOf(element)
    override fun isEmpty() = immutable.isEmpty()
    override fun iterator() = immutable.iterator()
    override fun lastIndexOf(element: T) = immutable.lastIndexOf(element)
    override fun listIterator() = immutable.listIterator()
    override fun listIterator(index: Int) = immutable.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int) = immutable.subList(fromIndex, toIndex)
    override fun equals(other: Any?): Boolean = immutable == other
    override fun hashCode() = immutable.hashCode()
    override fun toString() = "$immutable"

    override val mutable = object : AbstractMutableList<T>() {
        val real = initial.toMutableList()

        override val size get() = real.size

        override fun get(index: Int) = real[index]

        override fun addAll(index: Int, elements: Collection<T>) = doChange {
            real.addAll(index, elements).also {
                record(ListChange.Insert(index, elements.toList()))
            }
        }

        override fun add(index: Int, element: T) {
            doChange {
                real.add(index, element).also {
                    record(ListChange.Insert(index, listOf(element)))
                }
            }
        }

        override fun removeAt(index: Int) = doChange {
            real.removeAt(index).also {
                record(ListChange.Remove(index, it))
            }
        }

        override fun set(index: Int, element: T) = doChange {
            real.set(index, element).also {
                record(ListChange.Replace(index, remove = it, add = element))
            }
        }
    }

    /** Add a [value] to the end of this list, returning true to indicate the list was changed. */
    suspend inline fun add(value: T): Boolean =
        use { add(value) }

    /** Add all elements of the collection to the end of this list, returning true if the list was changed. */
    suspend inline fun addAll(elements: Collection<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the iterable to the end of this list, returning true if the list was changed. */
    suspend inline fun addAll(elements: Iterable<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the array to the end of this list, returning true if the list was changed. */
    suspend inline fun addAll(elements: Array<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the sequence to the end of this list, returning true if the list was changed. */
    suspend inline fun addAll(elements: Sequence<T>): Boolean =
        use { addAll(elements) }

    /** Remove [value] from this list, returning true if it was present and false if it was not. */
    suspend inline fun remove(value: T) = use { remove(value) }

    /** Remove all matching elements in [elements] from this list, returning true if the list was changed. */
    suspend inline fun removeAll(elements: Iterable<T>): Boolean =
        use { removeAll(elements) }

    /** Remove all matching elements in [elements] from this list, returning true if the list was changed. */
    suspend inline fun removeAll(elements: Array<T>): Boolean =
        use { removeAll(elements) }

    /** Remove all matching elements in [elements] from this list, returning true if the list was changed. */
    suspend inline fun removeAll(elements: Sequence<T>): Boolean =
        use { removeAll(elements) }

    /**
     * Retains only the elements in this list that are found in the collection, returning true if the list was
     * changed. */
    suspend inline fun retainAll(elements: Iterable<T>): Boolean =
        use { retainAll(elements) }

    /** Clear all values from this list. */
    override suspend fun clear() = use { clear() }

    /** Add [element] to this watchable collection. */
    suspend inline operator fun plusAssign(element: T) {
        add(element)
    }

    /** Adds all elements of the given [elements] collection to this watchable collection. */
    suspend inline operator fun plusAssign(elements: Iterable<T>) {
        addAll(elements)
    }

    /** Adds all elements of the given [elements] collection to this list. */
    suspend inline operator fun plusAssign(elements: Array<T>) { addAll(elements) }

    /** Adds all elements of the given [elements] collection to this list. */
    suspend inline operator fun plusAssign(elements: Sequence<T>) { addAll(elements) }

    /** Remove [element] from this watchable collection. */
    suspend inline operator fun minusAssign(element: T) { add(element) }

    /** Remove all elements of the given [elements] collection from this list. */
    suspend inline operator fun minusAssign(elements: Iterable<T>) { removeAll(elements) }

    /** Remove all elements of the given [elements] collection from this list. */
    suspend inline operator fun minusAssign(elements: Array<T>) { removeAll(elements) }

    /** Remove all elements of the given [elements] collection from this list. */
    suspend inline operator fun minusAssign(elements: Sequence<T>) { removeAll(elements) }

    override fun MutableList<T>.toImmutable() = toList()

    override fun List<T>.toInitialChange() = ListChange.Initial(this)

    override fun MutableList<T>.applyBoundChange(change: ListChange<T>) {
        when (change) {
            is ListChange.Initial -> { clear(); addAll(change.list) }
            is ListChange.Insert -> addAll(change.index, change.insert)
            is ListChange.Remove -> removeAt(change.index)
            is ListChange.Replace -> set(change.index, change.add)
        }
    }

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this { }
}
