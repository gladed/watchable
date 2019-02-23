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

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<Set<T>, SetChange<T>>(coroutineContext, this@WatchableSet) {
        override val initialChange
            get() = SetChange.Initial(set.toSet())

        override fun onBoundChange(change: SetChange<T>) {
            when (change) {
                is SetChange.Initial -> {
                    clear()
                    addAll(change.initial)
                }
                is SetChange.Add -> add(change.added)
                is SetChange.Remove -> remove(change.removed)
            }
        }
    }

    override val boundTo: Watchable<Set<T>, SetChange<T>>?
        get() = delegate.boundTo

    override val size: Int
        get() = synchronized(this) { set.size }

    override fun add(element: T): Boolean =
        delegate.changeOrNull {
            if (set.contains(element)) null else {
                set.add(element)
                SetChange.Add(element)
            }
        } is SetChange.Add

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val underlying: MutableIterator<T> = synchronized(this@WatchableSet) {
            set.iterator()
        }

        /** A cache of the prior return from [next]. */
        var last: T? = null

        override fun hasNext() = synchronized(this@WatchableSet) {
            underlying.hasNext()
        }

        override fun next() = synchronized(this@WatchableSet) {
            underlying.next().also { last = it }
        }

        override fun remove() {
            delegate.change {
                underlying.remove()
                SetChange.Remove(last ?: throw IllegalStateException("No last element"))
            }
        }
    }

    override fun CoroutineScope.watch(block: (SetChange<T>) -> Unit) =
        delegate.watchOwner(this@watch, block)

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableSet<T> = object : ReadOnlyWatchableSet<T> by this {
        override fun toString() =
            "ReadOnlyWatchableSet(${super.toString()})"
    }

    override fun bind(other: Watchable<Set<T>, SetChange<T>>) = delegate.bind(other)

    override fun unbind() = delegate.unbind()

    override fun toString() =
        "WatchableSet(${super.toString()})"
}
