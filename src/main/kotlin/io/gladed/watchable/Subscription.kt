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
import kotlinx.coroutines.channels.ReceiveChannel

/** A subscription to a channel of events that can be closed or cancelled. */
interface Subscription<C> : SubscriptionHandle, ReceiveChannel<List<C>> {
    /** Consume batches of values from this subscription. */
    fun batch(scope: CoroutineScope, periodMillis: Long = 0, block: suspend (List<C>) -> Unit): SubscriptionHandle
}
