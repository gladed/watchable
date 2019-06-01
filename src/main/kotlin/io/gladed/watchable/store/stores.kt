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

package io.gladed.watchable.store

import io.gladed.watchable.DeferredWatcher
import io.gladed.watchable.MapChange
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.Watcher
import io.gladed.watchable.util.guard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

const val DEFAULT_CONTAINER_PERIOD = 1000L

/**
 * Return a [HoldingStore] around this [Store].
 */
fun <T : Any> Store<T>.holding(
    context: CoroutineContext,
    containerPeriod: Long = DEFAULT_CONTAINER_PERIOD,
    start: suspend HoldBuilder.(T) -> Unit
) =
    HoldingStore(context, this, containerPeriod, start)

/** Derive a [Store] specific to this CoroutineScope from the supplied [holdingStore]. */
fun <T : Any> CoroutineScope.create(holdingStore: HoldingStore<T>): Store<T> =
    holdingStore.create(this)

/**
 * Return a [HoldingStore] for this [CoroutineScope] around [store].
 */
fun <T : Any> CoroutineScope.holding(
    store: Store<T>,
    containerPeriod: Long = DEFAULT_CONTAINER_PERIOD,
    start: suspend HoldBuilder.(T) -> Unit
) = HoldingStore(coroutineContext, store, containerPeriod, start)

/**
 * Return a memory cached version of this [Store].
 */
fun <T : Any> Store<T>.cached(context: CoroutineContext) = Cache(context, this)

/** Expose this [Store] of [T] items as a [Store] of transformed items [U]. */
fun <U : Any, T : Any> Store<T>.transform(transformer: Transformer<T, U>): Store<U> = object : Store<U> {
    override suspend fun get(key: String): U =
        transformer.toTarget(this@transform.get(key))

    override suspend fun put(key: String, value: U) {
        this@transform.put(key, transformer.fromTarget(value))
    }

    override suspend fun remove(key: String) {
        this@transform.remove(key)
    }

    override fun keys() = this@transform.keys()
}

/**
 * Load up a [WatchableMap] with all items in this [Store], and persisting changes from the map to the store
 * as they happen until [scope] completes.
 *
 * Changes to items implementing [Container] will trigger a put into the store.
 *
 * This is a one-way bind; external changes to the [Store] will not be reflected in [map].
 */
@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun <T : Any> CoroutineScope.bind(store: Store<T>, map: WatchableMap<String, T>, period: Long): Watcher {
    val setup = async {
        val containerWatchers = mutableMapOf<String, Watcher>().guard()

        fun putOnChange(key: String, item: Container) =
            item.watchables.batch(this@bind, period) { changes ->
                if (changes.any { !it.isInitial }) {
                    @Suppress("UNCHECKED_CAST")
                    store.put(key, item as T)
                }
            }

        // Empty the map
        map.clear()

        // Load up initial set
        store.keys().collect { key ->
            store.get(key).also { item ->
                if (item is Container) {
                    containerWatchers {
                        put(key, putOnChange(key, item))
                    }
                }
                map.put(key, item)
            }
        }

        // Watch for changes to map, pushing any change back to the store
        map.batch(this@bind, period) { changes ->
            for (change in changes) {
                when (change) {
                    // Ignore MapChange.Initial
                    is MapChange.Remove -> {
                        containerWatchers { remove(change.key) }?.cancel()
                        store.remove(change.key)
                    }
                    is MapChange.Put -> {
                        val item = change.add
                        containerWatchers { remove(change.key) }?.cancel()
                        if (item is Container) {
                            containerWatchers { put(change.key, putOnChange(change.key, item)) }
                        }
                        store.put(change.key, item)
                    }
                }
            }
        }
    }

    return DeferredWatcher(setup)
}
