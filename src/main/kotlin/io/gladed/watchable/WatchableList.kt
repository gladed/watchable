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
    override fun toString() = "WatchableList($immutable)"

    override val mutable = object : AbstractMutableList<T>() {
        val real = initial.toMutableList()

        override val size get() = real.size

        override fun get(index: Int) = real[index]

        override fun addAll(index: Int, elements: Collection<T>) = doChange {
            real.addAll(index, elements).also {
                changes.add(ListChange.Insert(index, elements.toList()))
            }
        }

        override fun add(index: Int, element: T) {
            doChange {
                real.add(index, element).also {
                    changes.add(ListChange.Insert(index, listOf(element)))
                }
            }
        }

        override fun removeAt(index: Int) = doChange {
            real.removeAt(index).also {
                changes.add(ListChange.Remove(index..index))
            }
        }

        override fun set(index: Int, element: T) = doChange {
            real.set(index, element).also {
                changes.add(ListChange.Replace(index, listOf(element)))
            }
        }
    }

    /** Add a [value] to the end of this list, returning true to indicate the list was changed. */
    suspend fun add(value: T): Boolean =
        use { add(value) }

    /** Add all elements of the collection to the end of this list, returning true if the list was changed. */
    suspend fun addAll(elements: Collection<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the iterable to the end of this list, returning true if the list was changed. */
    suspend fun addAll(elements: Iterable<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the array to the end of this list, returning true if the list was changed. */
    suspend fun addAll(elements: Array<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the sequence to the end of this list, returning true if the list was changed. */
    suspend fun addAll(elements: Sequence<T>): Boolean =
        use { addAll(elements) }

    /** Remove [value] from this list, returning true if it was present and false if it was not. */
    suspend fun remove(value: T) = use { remove(value) }

    /** Remove all matching elements in the collection from the list, returning true if the list was changed. */
    suspend fun removeAll(elements: Collection<T>): Boolean =
        use { removeAll(elements) }

    /**
     * Retains only the elements in this list that are found in the collection, returning true if the list was
     * changed. */
    suspend fun retainAll(elements: Collection<T>): Boolean =
        use { retainAll(elements) }

    /** Clear all values from this list. */
    override suspend fun clear() = use { clear() }

    override fun MutableList<T>.toImmutable() = toList()

    override fun List<T>.toInitialChange() = takeIf { isNotEmpty() }?.let {
        ListChange.Insert(0, toList())
    }

    override fun MutableList<T>.applyBoundChange(change: ListChange<T>) {
        when (change) {
            is ListChange.Insert -> addAll(change.index, change.items)
            is ListChange.Remove -> (change.range).forEach { _ -> removeAt(change.range.first) }
            is ListChange.Replace -> (change.items).forEachIndexed { index, element ->
                this[change.index + index] = element
            }
        }
    }

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() = "ReadOnlyWatchableList($immutable)"
    }
}
