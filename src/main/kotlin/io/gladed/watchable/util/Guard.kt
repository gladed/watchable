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

/** Protects all access to [item] behind a [Mutex]. */
class Guard<T>(private val item: T) {
    private val mutex = Mutex()

    /** Operate directly on [item] while holding a [Mutex]. */
    suspend operator fun <U> invoke(func: suspend T.() -> U): U =
        mutex.withLock { item.func() }
}
