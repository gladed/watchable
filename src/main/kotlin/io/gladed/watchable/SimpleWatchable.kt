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
import kotlinx.coroutines.isActive

/**
 * A [Watchable] that allows for a more verbose series of simpler changes.
 */
interface SimpleWatchable<S, C : HasSimpleChange<S>> : Watchable<C> {
    fun simple(scope: CoroutineScope, func: suspend (S) -> Unit): Watcher =
        watch(scope) {
            for (simpleChange in it.simple) {
                if (scope.isActive) func(simpleChange) else break
            }
        }
}
