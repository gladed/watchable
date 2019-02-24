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

package com.gladed.watchable

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * A thread-safe, mutable list whose contents may be watched for changes and/or bound to other maps for the duration
 * of its [coroutineContext]. Insertion order is preserved on iteration.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableList<T>(
    override val coroutineContext: CoroutineContext,
    initialValues: Collection<T> = emptyList()
) : AbstractMutableList<T>(), ReadOnlyWatchableList<T>, Bindable<List<T>, ListChange<T>> {

    /** The current list content. */
    private val list = initialValues.toMutableList()

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<List<T>, ListChange<T>>(coroutineContext, this@WatchableList) {
        override val initialChange
            get() = ListChange.Initial(list.toList())

        override fun onBoundChange(change: ListChange<T>) {
            when (change) {
                is ListChange.Initial<T> -> {
                    clear()
                    addAll(change.initial)
                }
                is ListChange.Add<T> -> add(change.index, change.added)
                is ListChange.Remove<T> -> removeAt(change.index)
                is ListChange.Replace<T> -> set(change.index, change.added)
            }
        }
    }

    override val boundTo: Watchable<List<T>, ListChange<T>>?
        get() = delegate.boundTo

    override val size: Int
        get() = synchronized(this) { list.size }

    override fun add(index: Int, element: T) {
        delegate.change {
            list.add(index, element)
            ListChange.Add(index, element)
        }
    }

    override fun get(index: Int) = synchronized(this) {
        list[index]
    }

    override fun removeAt(index: Int): T = delegate.change {
        val removed = list.removeAt(index)
        ListChange.Remove(index, removed)
    }.removed

    override fun set(index: Int, element: T): T =
        delegate.change {
            val removed = list[index]
            list[index] = element
            ListChange.Replace(index, removed, element)
        }.removed

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val underlying: MutableIterator<T> = synchronized(this) {
            list.iterator()
        }

        /** A cache of the prior return from [next]. */
        var last: T? = null

        /** Current index. */
        var index: Int = -1

        override fun hasNext() = synchronized(this) {
            underlying.hasNext()
        }

        override fun next() = synchronized(this) {
            underlying.next().also {
                index++
                last = it
            }
        }

        override fun remove() {
            delegate.change {
                underlying.remove()
                ListChange.Remove(index, last ?: throw IllegalStateException("No last element"))
            }
        }
    }

    override fun CoroutineScope.watch(block: (ListChange<T>) -> Unit) =
        delegate.watchOwner(this@watch, block)

    override fun bind(other: Watchable<List<T>, ListChange<T>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() =
            "ReadOnlyWatchableList(${super.toString()})"
    }

    override fun toString() =
        "WatchableList(${super.toString()})"
}
