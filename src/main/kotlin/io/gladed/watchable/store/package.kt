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

import io.gladed.watchable.Watcher
import kotlin.coroutines.CoroutineContext

/**
 * Return a [HoldingStore] around this [Store].
 */
fun <T : Any> Store<T>.holding(context: CoroutineContext, start: suspend (T) -> Hold) =
    HoldingStore(context, this, start)

/** Throw an exception to complain that something cannot be done. */
fun cannot(doSomething: String): Nothing = throw Cannot(doSomething)

/**
 * Return a memory cached version of this [Store].
 */
fun <T : Any> Store<T>.cached(context: CoroutineContext) = Cache(context, this)

/** Combine the behaviors of this [Hold] object with [other]. */
operator fun Hold.plus(other: Hold) = Hold(
    onStart = { onStart(); other.onStart() },
    onStop = { onStop(); other.onStop() },
    onCancel = { onCancel(); other.onCancel() },
    onRemove = { onRemove(); other.onRemove() },
    onCreate = { onCreate(); other.onCreate() }
)

/** Combine the behaviors of this [Hold] object with a [Watcher]. */
operator fun Hold.plus(other: Watcher) = Hold(
    onStart = { onStart(); other.start() },
    onStop = { onStop(); other.stop() },
    onCancel = { onCancel(); other.cancel() },
    onRemove = { onRemove() },
    onCreate = { onCreate() }
)

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
