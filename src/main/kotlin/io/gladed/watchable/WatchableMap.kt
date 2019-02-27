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
 * A mutable map whose contents may be watched for changes and/or bound to other maps for the duration
 * of its [coroutineContext]. Insertion order is preserved on iteration.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableMap<K, V>(
    override val coroutineContext: CoroutineContext,
    elements: Map<K, V> = emptyMap()
) : ReadOnlyWatchableMap<K, V>, Bindable<Map<K, V>, MapChange<K, V>> {

    /** The most current map content. */
    @Volatile private var current: Map<K, V>? = elements.toMap()

    override val map
        get() = current ?: synchronized(mutableMap) {
            mutableMap.toMap().also { current = it }
        }

    /** The internal mutable map representing the most current state. */
    private val mutableMap: MutableMap<K, V> = map.toMutableMap()

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
     * A mutable map which duplicates [map] into [mutableMap] if a change is made, and delivers changes
     * made to the delegate.
     */
    private inner class Mutator : AbstractMutableMap<K, V>() {

        val changes = mutableListOf<MapChange<K, V>>()

        override val size: Int
            get() = mutableMap.size

        fun deliver() {
            // If there was a change swap it into map
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

        override val entries = object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
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

                // TODO: This isn't quite enough
                override fun next() = underlying.next().also { last = it }

                override fun remove() {
                    delegate.checkChange()
                    underlying.remove()
                    last?.also {
                        changes.add(MapChange.Remove(it.key, it.value))
                    }
                }
            }
        }
    }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<Map<K, V>, MapChange<K, V>>(coroutineContext, this@WatchableMap) {
        override val initialChange
            get() = MapChange.Initial(map.toMap())

        override fun onBoundChanges(changes: List<MapChange<K, V>>) {
            changes.forEach { change ->
                when (change) {
                    is MapChange.Initial -> {
                        mutableMap.clear()
                        mutableMap.putAll(change.initial)
                    }
                    is MapChange.Add -> mutableMap[change.key] = change.added
                    is MapChange.Remove -> mutableMap.remove(change.key)
                    is MapChange.Replace -> mutableMap[change.key] = change.added
                }
            }
        }
    }

    override val boundTo: Watchable<Map<K, V>, MapChange<K, V>>?
        get() = delegate.boundTo

//    override fun put(key: K, value: V): V? =
//        delegate.changeOrNull {
//            map[key].let {
//                when (it) {
//                    value -> null // No Change
//                    null -> {
//                        map[key] = value
//                        MapChange.Add(key, value)
//                    }
//                    else -> {
//                        map[key] = value
//                        MapChange.Replace(key, it, value)
//                    }
//                }
//            }
//        }?.let {
//            // Return the removed value if any
//            (it as? MapChange.Remove<K, V>)?.removed
//        }
//
//    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> =
//        object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
//            override val size: Int
//                get() = map.entries.size
//
//            override fun add(element: MutableMap.MutableEntry<K, V>) =
//                // Kotlin MutableMap doesn't support this so we won't either
//                throw UnsupportedOperationException()
//
//            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> =
//                object : MutableIterator<MutableMap.MutableEntry<K, V>> {
//                    val underlying = map.entries.iterator()
//                    lateinit var last: MutableMap.MutableEntry<K, V>
//
//                    override fun hasNext() = underlying.hasNext()
//
//                    override fun next(): MutableMap.MutableEntry<K, V> =
//                        underlying.next()
//                            .also { last = it }
//                            .let { origin ->
//                                // Return a wrapped version of origin so that we can detect removal attempts.
//                                object : MutableMap.MutableEntry<K, V> by origin {
//                                    override fun setValue(newValue: V) =
//                                        delegate.change {
//                                            MapChange.Replace(key, origin.setValue(newValue), newValue)
//                                        }.removed
//
//                                    override fun equals(other: Any?) = origin == other
//                                    override fun hashCode() = origin.hashCode()
//                                    override fun toString() = "($key, $value)"
//                                }
//                    }
//
//                    override fun remove() {
//                        delegate.change {
//                            underlying.remove()
//                            MapChange.Remove(last.key, last.value)
//                        }
//                    }
//                }
//        }

    override fun CoroutineScope.watchBatches(block: (List<MapChange<K, V>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    override fun bind(other: Watchable<Map<K, V>, MapChange<K, V>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    /** Return an unmodifiable form of this [WatchableMap]. */
    fun readOnly(): ReadOnlyWatchableMap<K, V> = object : ReadOnlyWatchableMap<K, V> by this {
        override fun toString() =
            "ReadOnlyWatchableMap(${super.toString()})"
    }

    override fun toString() =
        "WatchableMap(${super.toString()})"
}
