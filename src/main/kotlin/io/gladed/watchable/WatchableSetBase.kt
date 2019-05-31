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

import io.gladed.watchable.util.Guard
import io.gladed.watchable.util.guard

/** Internal implementation of [WatchableSet]. */
@Suppress("TooManyFunctions")
internal class WatchableSetBase<T>(
    initial: Iterable<T>
) : MutableWatchableBase<Set<T>, MutableSet<T>, SetChange<T>>(), WatchableSet<T> {
    override var immutable: Set<T> = initial.toSet()

    override val size get() = immutable.size
    override fun contains(element: T) = immutable.contains(element)
    override fun containsAll(elements: Collection<T>) = immutable.containsAll(elements)
    override fun isEmpty() = immutable.isEmpty()
    override fun iterator(): Iterator<T> = immutable.iterator()
    override fun equals(other: Any?) = immutable == other
    override fun hashCode() = immutable.hashCode()
    override fun toString() = "$immutable"

    override val mutable: Guard<MutableSet<T>> = object : AbstractMutableSet<T>() {
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
    }.guard()

    override fun MutableSet<T>.erase() = clear()

    override fun MutableSet<T>.toImmutable() = toSet()

    override fun Set<T>.toInitialChange() = SetChange.Initial(toSet())

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
