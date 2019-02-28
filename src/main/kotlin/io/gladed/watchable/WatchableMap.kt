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
import kotlin.coroutines.CoroutineContext

/**
 * A [Map] whose contents may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableMap<K, V>(
    override val coroutineContext: CoroutineContext,
    initialElements: Map<K, V>
) : AbstractMap<K, V>(), ReadOnlyWatchableMap<K, V>, Bindable<Map<K, V>, MapChange<K, V>> {

    /** The internal mutable map representing the most current state. */
    private val mutableMap: MutableMap<K, V> = initialElements.toMutableMap()

    /**
     * The current map content, which may be swapped out at any time. If null, it needs to be refreshed
     * from [mutableMap].
     */
    @Volatile private var current: Map<K, V>? = null

    // Note: It would be nicer and perhaps more performant to use Mutex instead of synchronized. But this would
    // require runBlocking here, and suspend on "use" and on other callbacks.

    private val map
        get() = current ?: synchronized(mutableMap) {
            mutableMap.toMap().also { current = it }
        }

    override val entries: Set<Map.Entry<K, V>>
        get() = map.entries

    private val mutator = Mutator()

    /**
     * Suspend until [func] can safely execute, reading and/or writing data within the map as desired
     * and returning [func]'s result. This [WatchableMap] must not be bound ([isBound] must return false).
     * [func] should not itself block but simply apply changes and return.
     */
    fun <U> use(func: MutableMap<K, V>.() -> U): U =
    // Async, execute block on the single-threaded context, join, and return result.
        synchronized(mutableMap) {
            useWhileSynced(func)
        }

    /** Handles a change. MUST be synchronized on mutableMap. */
    private fun <U> useWhileSynced(block: MutableMap<K, V>.() -> U): U =
        mutator.block().also {
            mutator.deliver()
        }

    /**
     * A mutable map which duplicates [map] into [mutableMap] if a change is made, and delivers any changes
     * made to the delegate.
     */
    private inner class Mutator : AbstractMutableMap<K, V>() {

        val changes = mutableListOf<MapChange<K, V>>()

        override val size: Int
            get() = mutableMap.size

        /** Deliver outstanding changes (while synchronized). */
        fun deliver() {
            // If there was a change swap it into map
            if (changes.isNotEmpty()) {
                // Destroy the current copy, forcing it to be repopulated from when mutableMap at a later point
                current = null

                // Capture a list of changes and deliver to the single-threaded dispatcher to guarantee order
                delegate.send(changes)
                changes.clear()
            }
        }

        override val entries: MutableSet<MutableMap.MutableEntry<K, V>> =
            object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
                override val size: Int
                    get() = mutableMap.size

                override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
                    // Kotlin MutableMap doesn't support this so we won't either
                    throw UnsupportedOperationException()
                }

                override fun iterator() = object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                    val underlying = mutableMap.iterator()
                    var last: MutableMap.MutableEntry<K, V>? = null

                    override fun hasNext() = underlying.hasNext()

                    override fun next(): MutableMap.MutableEntry<K, V> =
                        underlying.next()
                            // Cache last so that it can be removed if necessary
                            .also { last = it }
                            // Wrap the returned entry so that we can detect changes on it
                            .let { original -> object : MutableMap.MutableEntry<K, V> by original {
                                override fun setValue(newValue: V): V {
                                    delegate.checkChange()
                                    return original.setValue(newValue).also {
                                        changes.add(MapChange.Replace(key, it, newValue))
                                    }
                                }
                                // Override equals to compare with the original object
                                override fun equals(other: Any?) = original == other
                                override fun hashCode() = original.hashCode()
                                // Print nicely
                                override fun toString() = "($key, $value)"
                            }
                            }

                    override fun remove() {
                        delegate.checkChange()
                        underlying.remove()
                        last?.also {
                            changes.add(MapChange.Remove(it.key, it.value))
                        }
                    }
                }
            }

        override fun put(key: K, value: V): V? {
            delegate.checkChange()
            return mutableMap.put(key, value).also { oldValue ->
                if (oldValue == null) {
                    changes.add(MapChange.Add(key, value))
                } else {
                    changes.add(MapChange.Replace(key, oldValue, value))
                }
            }
        }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<Map<K, V>, MapChange<K, V>>(coroutineContext, this@WatchableMap) {
        override val initialChange
            get() = MapChange.Initial(map.toMap())

        override fun onBoundChanges(changes: List<MapChange<K, V>>) {
            synchronized(mutableMap) {
                useWhileSynced {
                    changes.forEach { change ->
                        when (change) {
                            is MapChange.Initial -> {
                                clear()
                                putAll(change.initial)
                            }
                            is MapChange.Add -> put(change.key, change.added)
                            is MapChange.Remove -> remove(change.key)
                            is MapChange.Replace -> put(change.key, change.added)
                        }
                    }
                }
            }
        }
    }

    override val boundTo: Watchable<Map<K, V>, MapChange<K, V>>?
        get() = delegate.boundTo

    override fun CoroutineScope.watchBatches(block: (List<MapChange<K, V>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    override fun bind(other: Watchable<Map<K, V>, MapChange<K, V>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    /** Return an unmodifiable form of this [WatchableMap]. */
    fun readOnly(): ReadOnlyWatchableMap<K, V> = object : ReadOnlyWatchableMap<K, V> by this {
        override fun equals(other: Any?) = map == other
        override fun hashCode() = map.hashCode()
        override fun toString() = "ReadOnlyWatchableMap($map)"
    }

    override fun equals(other: Any?) = map == other
    override fun hashCode() = map.hashCode()
    override fun toString() = "WatchableMap($map)"
}
