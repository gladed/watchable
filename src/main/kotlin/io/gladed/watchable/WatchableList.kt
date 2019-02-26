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
import kotlinx.coroutines.newSingleThreadContext
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

    /**
     * Suspend until [block] can safely execute, reading and writing data within the list as desired
     * and returning [block]'s result. This [WatchableList] must not be bound ([isBound] must return false).
     */
    suspend fun <U> use(block: MutableList<T>.() -> U): U =
        // Async, execute block on the single-threaded context, join, and return result.
        withContext(singleThreadContext) {
            if (isBound()) throw IllegalStateException("Watchable object is bound")
            write(block)
        }

    private suspend fun <U> unboundWrite(block: MutableList<T>.() -> U): U =
        withContext(singleThreadContext) {
            write(block)
        }

    private fun <U> write(block: MutableList<T>.() -> U): U =
        block(mutableList).also {
            // If there was a change swap it into list
            modified?.also {
                list = it
                modified = null
            }
            // TODO(#13): Deliver batched changes here if we do that. Need performance test for it.
        }

    var modified: MutableList<T>? = null

    /**
     * A mutable list which duplicates [list] into [modified] if a change is made, and delivers changes
     * made to the delegate.
     */
    private val mutableList: MutableList<T> = object : AbstractMutableList<T>() {
        val current: List<T> get() = modified ?: list

        // TODO(#13): batched changes for a possible performance gain? launch all changes at once...

        override val size: Int
            get() = modified?.size ?: list.size

        // Copy on write for the duration of this session
        private fun <U> change(func: MutableList<T>.() -> U) =
            (modified ?: list.toMutableList().also { modified = it }).func()

        override fun get(index: Int) = current[index]

        override fun add(index: Int, element: T) {
            change {
                add(index, element).also {
                    delegate.deliver(ListChange.Add(index, element))
                }
            }
        }

        override fun removeAt(index: Int) =
            change {
                removeAt(index).also {
                    delegate.deliver(ListChange.Remove(index, it))
                }
            }

        override fun set(index: Int, element: T) =
            change {
                set(index, element).also {
                    delegate.deliver(ListChange.Replace(index, it, element))
                }
            }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<List<T>, ListChange<T>>(coroutineContext, this@WatchableList) {
        override val initialChange
            get() = ListChange.Initial(list.toList())

        override fun onBoundChange(change: ListChange<T>) {
            when (change) {
                is ListChange.Initial<T> -> launch {
                        unboundWrite {
                            clear()
                            addAll(change.initial)
                        }
                    }

                is ListChange.Add<T> -> launch {
                    unboundWrite { add(change.index, change.added) }
                }
                is ListChange.Remove<T> -> launch {
                    unboundWrite { removeAt(change.index) }
                }
                is ListChange.Replace<T> -> launch {
                    unboundWrite { set(change.index, change.added) }
                }
            }
        }
    }

    override val boundTo: Watchable<List<T>, ListChange<T>>?
        get() = delegate.boundTo

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

    companion object {
        private val singleThreadContext = newSingleThreadContext("WatchableList")
    }
}
