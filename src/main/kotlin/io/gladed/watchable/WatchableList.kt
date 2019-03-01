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
 * A [List] whose contents may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableList<T>(
    override val coroutineContext: CoroutineContext,
    initial: Collection<T> = emptyList()
) : MutableWatchableBase<MutableList<T>, List<T>, ListChange<T>>(), ReadOnlyWatchableList<T> {

    override val mutable = object: AbstractMutableList<T>() {
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
        override fun toString() = "ReadOnlyWatchableList(})"
    }

    override fun toString() = "WatchableList()"
}
