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
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive

/**
 * Wraps type [T] so that it may be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> {

    /** Return an immutable copy of the current value of [T]. */
    val value: T

    /**
     * Return a subscription watching for changes to this watchable.
     */
    fun subscribe(scope: CoroutineScope): Subscription<C>

    /**
     * Deliver changes for this [Watchable] to [func], starting with its initial state, until the scope completes.
     */
    fun watch(scope: CoroutineScope, func: suspend (C) -> Unit): SubscriptionHandle =
        batch(scope) { changes ->
            for (change in changes) {
                if (scope.isActive) func(change) else break
            }
        }

    /**
     * Deliver periodic lists of changes for this [Watchable] to [func], starting with its initial state, until
     * the scope completes.
     */
    @UseExperimental(ObsoleteCoroutinesApi::class)
    fun batch(
        scope: CoroutineScope,
        /** The minimum time in millis between change notifications, or 0 (the default) for no delay. */
        minPeriod: Long = 0,
        func: suspend (List<C>) -> Unit
    ): SubscriptionHandle =
        subscribe(scope).also { subscription ->
            scope.daemon {
                batch(subscription.receiver, minPeriod).consumeEach { func(it) }
            }
        }
}
