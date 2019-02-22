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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A thread-safe, mutable list whose contents may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableList<T>(
    override val coroutineContext: CoroutineContext,
    initialValues: Collection<T> = emptyList()
) : AbstractMutableList<T>(), ReadOnlyWatchableList<T>, Bindable<List<T>, ListChange<T>> {
    /** The current list content. */
    private val list = initialValues.toMutableList()

    /** Channel receiving all changes made to the list. */
    private val channel by lazy {
        BroadcastChannel<ListChange<T>>(CAPACITY).also { cancelWithScope(it) }
    }

    override val size: Int
        get() = synchronized(this) { list.size }

    override fun add(index: Int, element: T) {
        synchronized(this) {
            binder.checkChange()
            list.add(index, element)
            launch {
                channel.send(ListChange.Add(index, element))
            }
        }
    }

    override fun get(index: Int) = synchronized(this) {
        list[index]
    }

    override fun removeAt(index: Int): T = synchronized(this) {
        binder.checkChange()
        list.removeAt(index).also { removed ->
            launch {
                channel.send(ListChange.Remove(index, removed))
            }
        }
    }

    override fun set(index: Int, element: T): T = synchronized(this) {
        binder.checkChange()
        list[index].also { removed ->
            list[index] = element
            launch {
                channel.send(ListChange.Replace(index, removed, element))
            }
        }
    }

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
            synchronized(this) {
                binder.checkChange()
                underlying.remove()
                launch {
                    channel.send(ListChange.Remove(index, last ?: throw IllegalStateException("No last element")))
                }
            }
        }
    }

    override fun CoroutineScope.watch(block: (ListChange<T>) -> Unit): Job {
        // Open first in case there are changes
        val sub = channel.openSubscription()
        val initial = ListChange.Initial(this@WatchableList.toList())

        // Send a copy of initial content
        return launch {
            block(initial)
            sub.consumeEach {
                block(it)
            }
        }
    }

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() =
            "ReadOnlyWatchableList(${super.toString()})"
    }

    private val binder = BindableBase(this@WatchableList) {
        when (it) {
            is ListChange.Initial<T> -> {
                clear()
                addAll(it.initial)
            }
            is ListChange.Add<T> -> add(it.index, it.added)
            is ListChange.Remove<T> -> removeAt(it.index)
            is ListChange.Replace<T> -> set(it.index, it.added)
        }
    }

    override val boundTo
        get() = binder.boundTo

    override fun bind(other: Watchable<List<T>, ListChange<T>>) {
        binder.bind(other)
    }

    override fun unbind() {
        binder.unbind()
    }

    override fun toString() =
        "WatchableList(${super.toString()})"

    companion object {
        private const val CAPACITY = 20
    }
}
