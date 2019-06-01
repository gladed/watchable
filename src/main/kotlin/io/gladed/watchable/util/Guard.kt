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

package io.gladed.watchable.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Provides mutually-exclusive access to a value of [T]. */
interface Guard<T> {
    /** Allows access to [T] while a [Mutex] is held. */
    suspend operator fun <U> invoke(func: suspend T.() -> U): U
}

/** Return [T] surrounded by a [Guard]. */
fun <T> T.guard(): Guard<T> = GuardBase { this }

/** Return a [Guard] that will lazily [create] [T] on first use. */
fun <T> lazyGuard(create: suspend () -> T): Guard<T> = GuardBase(create)

/** Protects all access to [item] behind a [Mutex]. */
private class GuardBase<T>(private val create: suspend () -> T) : Guard<T> {
    private val mutex = Mutex()
    private var item: T? = null

    /** Operate directly on the guarded item while holding a [Mutex]. */
    override suspend operator fun <U> invoke(func: suspend T.() -> U): U =
        mutex.withLock {
            (item ?: create().also { item = it }).func()
        }
}
