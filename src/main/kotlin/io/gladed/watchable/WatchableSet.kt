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

import kotlin.coroutines.CoroutineContext

/**
 * A [Set] whose contents may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableSet<T> internal constructor(
    override val coroutineContext: CoroutineContext,
    initial: Collection<T>
) : MutableWatchableBase<MutableSet<T>, Set<T>, SetChange<T>>(), ReadOnlyWatchableSet<T> {

    override val mutable = object : AbstractMutableSet<T>() {
        private val real = initial.toMutableSet()

        override val size get() = real.size

        override fun add(element: T) = doChange {
            real.add(element).also { success ->
                if (success) changes.add(SetChange.Add(element))
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
                    // Last cannot be null if remove() succeeded
                    changes.add(SetChange.Remove(last!!))
                }
            }
        }
    }

    override fun MutableSet<T>.toImmutable() = toSet()

    override fun Set<T>.toInitialChange() = SetChange.Initial(this)

    override fun MutableSet<T>.applyBoundChange(change: SetChange<T>) {
        when (change) {
            is SetChange.Initial -> {
                clear()
                addAll(change.initial)
            }
            is SetChange.Add -> add(change.added)
            is SetChange.Remove -> remove(change.removed)
        }
    }

    override fun replace(newValue: Set<T>) {
        mutable.clear()
        mutable.addAll(newValue)
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableSet<T> = object : ReadOnlyWatchableSet<T> by this {
        override fun toString() = "ReadOnlyWatchableSet()"
    }

    override fun toString() = "WatchableSet()"
}
