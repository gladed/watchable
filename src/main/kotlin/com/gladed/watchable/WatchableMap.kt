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
 * A thread-safe, mutable map whose contents may be watched for changes. Insertion order is preserved on iteration.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableMap<K, V>(
    override val coroutineContext: CoroutineContext,
    elements: Map<K, V> = emptyMap()
) : AbstractMutableMap<K, V>(), ReadOnlyWatchableMap<K, V>, Bindable<Map<K, V>, MapChange<K, V>> {

    /** The actual map to be used; must not be modified except through this object's accessors. */
    private val map = elements.toMutableMap()

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<Map<K, V>, MapChange<K, V>>(coroutineContext, this@WatchableMap) {
        override val initialChange
            get() = MapChange.Initial(map.toMap())

        override fun onBoundChange(change: MapChange<K, V>) {
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

    override val boundTo: Watchable<Map<K, V>, MapChange<K, V>>?
        get() = delegate.boundTo

    override val size: Int
        get() = synchronized(this) { map.size }

    override fun put(key: K, value: V): V? =
        delegate.changeOrNull {
            map[key].let {
                when (it) {
                    value -> null // No Change
                    null -> {
                        map[key] = value
                        MapChange.Add(key, value)
                    }
                    else -> {
                        map[key] = value
                        MapChange.Replace(key, it, value)
                    }
                }
            }
        }?.let {
            // Return the removed value if any
            (it as? MapChange.Remove<K, V>)?.removed
        }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
            override val size: Int
                get() = synchronized(this@WatchableMap) {
                    map.entries.size
                }

            override fun add(element: MutableMap.MutableEntry<K, V>) =
                // Kotlin MutableMap doesn't support this so we won't either
                throw UnsupportedOperationException()

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> =
                object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                    val underlying = map.entries.iterator()
                    lateinit var last: MutableMap.MutableEntry<K, V>

                    override fun hasNext() = underlying.hasNext()

                    override fun next(): MutableMap.MutableEntry<K, V> =
                        underlying.next()
                            .also { last = it }
                            .let { origin ->
                                // Return a wrapped version of origin so that we can detect removal attempts.
                                object : MutableMap.MutableEntry<K, V> by origin {
                                    override fun setValue(newValue: V) =
                                        delegate.change {
                                            MapChange.Replace(key, origin.setValue(newValue), newValue)
                                        }.removed

                                    override fun equals(other: Any?) = origin == other
                                    override fun hashCode() = origin.hashCode()
                                    override fun toString() = "($key, $value)"
                                }
                    }

                    override fun remove() {
                        delegate.change {
                            underlying.remove()
                            MapChange.Remove(last.key, last.value)
                        }
                    }
                }
        }

    override fun CoroutineScope.watch(block: (MapChange<K, V>) -> Unit) =
        delegate.watchOwner(this@watch, block)

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
