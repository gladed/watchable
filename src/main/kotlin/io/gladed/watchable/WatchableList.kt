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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A mutable list whose contents may be watched for changes and/or bound to other maps for the duration
 * of its [coroutineContext].
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableList<T>(
    override val coroutineContext: CoroutineContext,
    initialValues: Collection<T> = emptyList()
) : ReadOnlyWatchableList<T>, Bindable<List<T>, ListChange<T>> {

    /** The most current list content. */
    @Volatile private var current: List<T>? = initialValues.toList()

    override val list
        get() = current ?: synchronized(mutableList) {
            mutableList.toList().also { current = it }
        }

    /** The internal mutable list representing the most current state. */
    private val mutableList: MutableList<T> = list.toMutableList()

    private val mutator = Mutator()

    /**
     * Suspend until [func] can safely execute, reading and/or writing data within the list as desired
     * and returning [func]'s result. This [WatchableList] must not be bound ([isBound] must return false).
     * [func] should not itself block but simply apply changes and return.
     */
    fun <U> use(func: MutableList<T>.() -> U): U =
        // Async, execute block on the single-threaded context, join, and return result.
        synchronized(mutableList) {
            useWhileSynced(func)
        }

    /** Handles a change. MUST be synchronized on mutableList. */
    private fun <U> useWhileSynced(block: MutableList<T>.() -> U): U =
        mutator.block().also {
            mutator.deliver()
        }

    /**
     * A mutable list which duplicates [list] into [mutableList] if a change is made, and delivers changes
     * made to the delegate.
     */
    private inner class Mutator : AbstractMutableList<T>() {
        val changes = mutableListOf<ListChange<T>>()

        override val size: Int
            get() = mutableList.size

        fun deliver() {
            // If there was a change swap it into list
            if (changes.isNotEmpty()) {
                // Assign the local copy
                current = null

                // send() may suspend so we need to deliver changes all at once.
                changes.toList().also {
                    launch(coroutineContext) {
                        delegate.send(it)
                    }
                }
                changes.clear()
            }
        }

        override fun get(index: Int) = mutableList[index]

        override fun add(index: Int, element: T) {
            delegate.checkChange()
            mutableList.add(index, element).also {
                changes.add(ListChange.Add(index, element))
            }
        }

        override fun removeAt(index: Int): T {
            delegate.checkChange()
            return mutableList.removeAt(index).also {
                changes.add(ListChange.Remove(index, it))
            }
        }

        override fun set(index: Int, element: T): T {
            delegate.checkChange()
            return mutableList.set(index, element).also {
                changes.add(ListChange.Replace(index, it, element))
            }
        }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<List<T>, ListChange<T>>(coroutineContext, this@WatchableList) {
        override val initialChange
            get() = ListChange.Initial(list)

        override fun onBoundChanges(changes: List<ListChange<T>>) {
            synchronized(mutableList) {
                useWhileSynced {
                    changes.forEach { change ->
                        when (change) {
                            is ListChange.Initial<T> -> clear().also { addAll(change.initial) }
                            is ListChange.Add<T> -> add(change.index, change.added)
                            is ListChange.Remove<T> -> removeAt(change.index)
                            is ListChange.Replace<T> -> set(change.index, change.added)
                        }
                    }
                }
            }
        }
    }

    override val boundTo: Watchable<List<T>, ListChange<T>>?
        get() = delegate.boundTo

    override fun CoroutineScope.watchBatches(block: (List<ListChange<T>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    override fun bind(other: Watchable<List<T>, ListChange<T>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() =
            "ReadOnlyWatchableList($list})"
    }

    override fun toString() =
        "WatchableList($list)"
}
