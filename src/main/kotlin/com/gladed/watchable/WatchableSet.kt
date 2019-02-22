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
 * A thread-safe, mutable set whose contents may be watched for changes. Insertion order is preserved on iteration.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableSet<T>(
    override val coroutineContext: CoroutineContext,
    elements: Collection<T> = emptyList()
) : AbstractMutableSet<T>(), ReadOnlyWatchableSet<T>, Bindable<Set<T>, SetChange<T>> {

    /** The actual set to be used; must not be modified except through this object's accessors. */
    private val set = elements.toMutableSet()

    /** Channel receiving all changes made to the set. */
    private val channel by lazy {
        BroadcastChannel<SetChange<T>>(CAPACITY).also { cancelWithScope(it) }
    }

    override val size: Int
        get() = synchronized(set) { set.size }

    override fun add(element: T): Boolean =
        synchronized(set) {
            binder.checkChange()
            if (set.contains(element)) {
                false
            } else {
                set.add(element)
                launch {
                    channel.send(SetChange.Add(element))
                }
                true
            }
        }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val underlying: MutableIterator<T> = synchronized(set) {
            set.iterator()
        }

        /** A cache of the prior return from [next]. */
        var last: T? = null

        override fun hasNext() = synchronized(set) {
            underlying.hasNext()
        }

        override fun next() = synchronized(set) {
            underlying.next().also { last = it }
        }

        override fun remove() {
            synchronized(set) {
                binder.checkChange()
                underlying.remove()
                launch {
                    channel.send(SetChange.Remove(last ?: throw IllegalStateException("No last element")))
                }
            }
        }
    }

    override fun CoroutineScope.watch(block: (SetChange<T>) -> Unit): Job {
        // Open first in case there are changes
        val sub = channel.openSubscription()
        val initial = SetChange.Initial(this@WatchableSet.toSet())
        return launch {
            // Send a current copy of initial content
            block(initial)
            sub.consumeEach {
                block(it)
            }
        }
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableSet<T> = object : ReadOnlyWatchableSet<T> by this {
        override fun toString() =
            "ReadOnlyWatchableSet(${super.toString()})"
    }

    private val binder = BindableBase(this) {
        when (it) {
            is SetChange.Initial -> {
                clear()
                addAll(it.initial)
            }
            is SetChange.Add -> add(it.added)
            is SetChange.Remove -> remove(it.removed)
        }
    }

    override val boundTo
        get() = binder.boundTo

    override fun bind(other: Watchable<Set<T>, SetChange<T>>) {
        binder.bind(other)
    }

    override fun unbind() {
        binder.unbind()
    }

    override fun toString() =
        "WatchableSet(${super.toString()})"

    companion object {
        private const val CAPACITY = 20
    }
}
