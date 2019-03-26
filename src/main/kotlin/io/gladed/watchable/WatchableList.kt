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

    override val size: Int get() = value.size
    override fun contains(element: T) = value.contains(element)
    override fun containsAll(elements: Collection<T>) = value.containsAll(elements)
    override fun get(index: Int) = value[index]
    override fun indexOf(element: T) = value.indexOf(element)
    override fun isEmpty() = value.isEmpty()
    override fun iterator() = value.iterator()
    override fun lastIndexOf(element: T) = value.lastIndexOf(element)
    override fun listIterator() = value.listIterator()
    override fun listIterator(index: Int) = value.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int) = value.subList(fromIndex, toIndex)
    override fun equals(other: Any?): Boolean = value == other
    override fun hashCode() = value.hashCode()
    override fun toString() = "WatchableList($value)"

    override val mutable = object : AbstractMutableList<T>() {
        val real = initial.toMutableList()

        override val size get() = real.size

        override fun get(index: Int) = real[index]

        override fun add(index: Int, element: T) {
            doChange {
                real.add(index, element).also {
                    changes.add(ListChange.Add(index, element))
                }
            }
        }

        override fun removeAt(index: Int) = doChange {
            real.removeAt(index).also {
                changes.add(ListChange.Remove(index, it))
            }
        }

        override fun set(index: Int, element: T) = doChange {
            real.set(index, element).also {
                changes.add(ListChange.Replace(index, it, element))
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
    suspend fun clear() = use { clear() }

    override fun MutableList<T>.toImmutable() = toList()

    override fun List<T>.toInitialChange() = ListChange.Initial(this)

    override fun MutableList<T>.applyBoundChange(change: ListChange<T>) {
        when (change) {
            is ListChange.Initial -> {
                clear()
                addAll(change.initial)
            }
            is ListChange.Add -> add(change.index, change.added)
            is ListChange.Remove -> removeAt(change.index)
            is ListChange.Replace -> set(change.index, change.added)
        }
    }

    override fun replace(newValue: List<T>) {
        mutable.clear()
        mutable.addAll(newValue)
    }

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() = "ReadOnlyWatchableList($value)"
    }
}
