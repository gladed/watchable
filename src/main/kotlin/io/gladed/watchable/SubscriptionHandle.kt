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

/** A handle allowing for management of a subscription to a channel of events. */
interface SubscriptionHandle {

    /** Cancel the subscription immediately so that no further events are reported. */
    fun cancel()

    /** Close the subscription, allowing all outstanding events to be delivered first. */
    fun close()

    /** Suspend until the subscription is complete. */
    suspend fun join()

    /** Flush all events and close the channel (shorthand for [close] then [join]). */
    suspend fun closeAndJoin() {
        close()
        join()
    }
}
