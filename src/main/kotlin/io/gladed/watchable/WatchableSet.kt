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
 * A mutable set whose contents may be watched for changes and/or bound to other maps for the duration
 * of its [coroutineContext]. Insertion order is preserved on iteration.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableSet<T>(
    override val coroutineContext: CoroutineContext,
    elements: Collection<T> = emptyList()
) : ReadOnlyWatchableSet<T>, Bindable<Set<T>, SetChange<T>> {

    /** The actual set to be used; must not be modified except through this object's accessors. */
    @Volatile private var current: Set<T>? = elements.toMutableSet()

    override val set: Set<T>
        get() = current ?: synchronized(mutableSet) {
            mutableSet.toSet().also { current = it }
        }

    private val mutableSet = set.toMutableSet()

    private val mutator = Mutator()

    /**
     * Suspend until [func] can safely execute, reading and/or writing data within the set as desired
     * and returning [func]'s result. This [WatchableSet] must not be bound ([isBound] must return false).
     * [func] should not itself block but simply apply changes and return.
     */
    fun <U> use(func: MutableSet<T>.() -> U): U =
    // Async, execute block on the single-threaded context, join, and return result.
        synchronized(mutableSet) {
            useWhileSynced(func)
        }

    /** Handles a change. MUST be synchronized on mutableSet. */
    private fun <U> useWhileSynced(block: MutableSet<T>.() -> U): U =
        mutator.block().also {
            mutator.deliver()
        }

    /**
     * A mutable set which duplicates [set] into [mutableSet] if a change is made, and delivers changes
     * made to the delegate.
     */
    private inner class Mutator : AbstractMutableSet<T>() {
        val changes = mutableListOf<SetChange<T>>()

        override val size: Int
            get() = mutableSet.size

        fun deliver() {
            // If there was a change swap it into set
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

        override fun add(element: T): Boolean {
            delegate.checkChange()
            return mutableSet.add(element).also {
                if (it) {
                    changes.add(SetChange.Add(element))
                }
            }
        }

        override fun iterator(): MutableIterator<T> {
            return object : MutableIterator<T> {
                val underlying: MutableIterator<T> = mutableSet.iterator()

                /** A cache of the prior return from [next]. */
                var last: T? = null

                override fun hasNext() = underlying.hasNext()

                override fun next() = underlying.next().also { last = it }

                override fun remove() {
                    delegate.checkChange()
                    underlying.remove()
                    changes.add(SetChange.Remove(last ?: throw IllegalStateException("No last element")))
                }
            }
        }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<Set<T>, SetChange<T>>(coroutineContext, this@WatchableSet) {
        override val initialChange
            get() = SetChange.Initial(set.toSet())

        override fun onBoundChanges(changes: List<SetChange<T>>) {
            synchronized(mutableSet) {
                useWhileSynced {
                    changes.forEach { change ->
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
            }
        }
    }

    override val boundTo: Watchable<Set<T>, SetChange<T>>?
        get() = delegate.boundTo

    override fun CoroutineScope.watchBatches(block: (List<SetChange<T>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableSet<T> = object : ReadOnlyWatchableSet<T> by this {
        override fun toString() =
            "ReadOnlyWatchableSet($set})"
    }

    override fun bind(other: Watchable<Set<T>, SetChange<T>>) = delegate.bind(other)

    override fun unbind() = delegate.unbind()

    override fun toString() =
        "WatchableSet($set})"
}
