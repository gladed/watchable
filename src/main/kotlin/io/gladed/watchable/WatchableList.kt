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

import io.gladed.watchable.Watchable.Companion.writeContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext

/**
 * A mutable list whose contents may be watched for changes and/or bound to other maps for the duration
 * of its [coroutineContext].
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableList<T>(
    override val coroutineContext: CoroutineContext,
    initialValues: Collection<T> = emptyList()
) : ReadOnlyWatchableList<T>, Bindable<List<T>, ListChange<T>> {

    /** The most current list content. */
    @Volatile override var list = initialValues.toList()
        private set(value) { field = value }

    private val mutator = Mutator()

    /**
     * Suspend until [block] can safely execute, reading and writing data within the list as desired
     * and returning [block]'s result. This [WatchableList] must not be bound ([isBound] must return false).
     */
    suspend fun <U> use(block: MutableList<T>.() -> U): U =
        // Async, execute block on the single-threaded context, join, and return result.
        withContext(writeContext) {
            if (isBound()) throw IllegalStateException("Watchable object is bound")
            write(block)
        }

    /** Handles a change. Must be on writeContext. */
    private suspend fun <U> write(block: MutableList<T>.() -> U): U =
        mutator.block().also {
            mutator.deliver()
        }

    /**
     * A mutable list which duplicates [list] into [modified] if a change is made, and delivers changes
     * made to the delegate.
     */
    private inner class Mutator : AbstractMutableList<T>() {
        var modified: MutableList<T>? = null
        val current: List<T> get() = modified ?: list
        val changes = mutableListOf<ListChange<T>>()

        override val size: Int
            get() = modified?.size ?: list.size

        suspend fun deliver() {
            // If there was a change swap it into list
            modified?.also { newList ->
                // Push the modified list back into the outer object
                list = newList
                modified = null

                // Deliver and clear the changes
                val toDeliver = changes.toList()
                changes.clear()

                // send() may suspend so we need to deliver changes all at once.
                delegate.send(toDeliver)
            }
        }

        // Copy on write for the duration of this session
        private fun <U> change(func: MutableList<T>.() -> U) =
            (modified ?: list.toMutableList().also { modified = it }).func()

        override fun get(index: Int) = current[index]

        override fun add(index: Int, element: T) {
            change {
                add(index, element).also {
                    changes.add(ListChange.Add(index, element))
                }
            }
        }

        override fun removeAt(index: Int) =
            change {
                removeAt(index).also {
                    changes.add(ListChange.Remove(index, it))
                }
            }

        override fun set(index: Int, element: T) =
            change {
                set(index, element).also {
                    changes.add(ListChange.Replace(index, it, element))
                }
            }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<List<T>, ListChange<T>>(coroutineContext, this@WatchableList) {
        override val initialChange
            get() = ListChange.Initial(list.toList())

        override fun onBoundChange(change: ListChange<T>) {
            launch(writeContext) {
                write {
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

    override val boundTo: Watchable<List<T>, ListChange<T>>?
        get() = delegate.boundTo

    override fun CoroutineScope.watchBatches(block: suspend (List<ListChange<T>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    override fun bind(other: Watchable<List<T>, ListChange<T>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    /** Return an unmodifiable form of this [WatchableList]. */
    fun readOnly(): ReadOnlyWatchableList<T> = object : ReadOnlyWatchableList<T> by this {
        override fun toString() =
            "ReadOnlyWatchableList(${super.toString()})"
    }

//    override fun toString() =
//        "WatchableList(${super.toString()})"
}
