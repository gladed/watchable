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

/** A [Watchable] wrapper for a [Set] which may also be modified or bound. Use [watchableSetOf] to create. */
@Suppress("TooManyFunctions")
class WatchableSet<T> internal constructor(
    initial: Collection<T>
) : MutableWatchableBase<Set<T>, T, MutableSet<T>, SetChange<T>>(), ReadOnlyWatchableSet<T> {
    override var immutable: Set<T> = initial.toSet()

    override val size get() = immutable.size
    override fun contains(element: T) = immutable.contains(element)
    override fun containsAll(elements: Collection<T>) = immutable.containsAll(elements)
    override fun isEmpty() = immutable.isEmpty()
    override fun iterator(): Iterator<T> = immutable.iterator()
    override fun equals(other: Any?) = immutable == other
    override fun hashCode() = immutable.hashCode()
    override fun toString() = "$immutable"

    override val mutable = object : AbstractMutableSet<T>() {
        private val real = initial.toMutableSet()

        override val size get() = real.size

        override fun add(element: T) = doChange {
            real.add(element).also { success ->
                if (success) record(SetChange.Add(listOf(element)))
            }
        }

        override fun iterator() = object : MutableIterator<T> {
            val realIterator: MutableIterator<T> = real.iterator()

            /** A cache of the prior return from [next]. */
            var last: T? = null

            override fun hasNext() = realIterator.hasNext()

            override fun next() = realIterator.next().also { last = it }

            override fun remove() {
                doChange {
                    realIterator.remove()
                    // Last must be OK if remove() didn't throw
                    @Suppress("UNCHECKED_CAST")
                    record(SetChange.Remove(listOf(last as T)))
                }
            }
        }
    }

    /** Add a [value] to the end of this set, returning true if the set was changed. */
    suspend fun add(value: T): Boolean =
        use { add(value) }

    /** Add all elements of the collection to the end of this set, returning true if the set was changed. */
    suspend fun addAll(elements: Collection<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the iterable to the end of this set, returning true if the set was changed. */
    suspend fun addAll(elements: Iterable<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the array to the end of this set, returning true if the set was changed. */
    suspend fun addAll(elements: Array<T>): Boolean =
        use { addAll(elements) }

    /** Add all elements of the sequence to the end of this set, returning true if the set was changed. */
    suspend fun addAll(elements: Sequence<T>): Boolean =
        use { addAll(elements) }

    /** Remove [value] from this set, returning true if it was present and false if it was not. */
    suspend fun remove(value: T) = use { remove(value) }

    /** Remove all matching elements in the collection from the set, returning true if the set was changed. */
    suspend fun removeAll(elements: Iterable<T>): Boolean =
        use { removeAll(elements) }

    /** Remove all matching elements in the collection from the set, returning true if the set was changed. */
    suspend fun removeAll(elements: Array<T>): Boolean =
        use { removeAll(elements) }

    /** Remove all matching elements in the collection from the set, returning true if the set was changed. */
    suspend fun removeAll(elements: Sequence<T>): Boolean =
        use { removeAll(elements) }

    /**
     * Retains only the elements in this set that are found in the collection, returning true if the set was
     * changed.
     */
    suspend fun retainAll(elements: Iterable<T>): Boolean =
        use { retainAll(elements) }

    /** Clear all values from this set. */
    override suspend fun clear() = use { clear() }

    suspend inline operator fun plusAssign(value: T) { add(value) }

    suspend inline operator fun plusAssign(elements: Array<T>) { addAll(elements) }

    suspend inline operator fun plusAssign(elements: Iterable<T>) { addAll(elements) }

    suspend inline operator fun plusAssign(elements: Sequence<T>) { addAll(elements) }

    suspend inline operator fun minusAssign(value: T) { remove(value) }

    suspend inline operator fun minusAssign(elements: Array<T>) { removeAll(elements) }

    suspend inline operator fun minusAssign(elements: Iterable<T>) { removeAll(elements) }

    suspend inline operator fun minusAssign(elements: Sequence<T>) { removeAll(elements) }

    override fun MutableSet<T>.toImmutable() = toSet()

    override fun Set<T>.toInitialChange() = takeIf { isNotEmpty() }?.let {
        SetChange.Initial(toSet())
    }

    override fun MutableSet<T>.applyBoundChange(change: SetChange<T>) {
        when (change) {
            is SetChange.Initial -> { clear(); addAll(change.set) }
            is SetChange.Remove -> removeAll(change.remove)
            is SetChange.Add -> addAll(change.add)
        }
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    override fun readOnly(): ReadOnlyWatchableSet<T> = object : ReadOnlyWatchableSet<T> by this { }
}
