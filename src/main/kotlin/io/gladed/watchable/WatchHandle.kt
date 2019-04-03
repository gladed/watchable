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

/** Allows management over a watch operation. */
interface WatchHandle {

    /** Cancel the watch operation immediately so that no further changes are handled. */
    fun cancel()

    /** Close the watch operation, allowing all outstanding changes to be delivered first. */
    fun close()

    /** Suspend until all outstanding changes are drained and the watch operation is completed. */
    suspend fun join()

    /** Close the operation with [close] and wait for all events to be processed with [join]. */
    suspend fun closeAndJoin() {
        close()
        join()
    }

    /** Merge two [WatchHandle] objects, returning a single one that spans both. */
    operator fun plus(right: WatchHandle): WatchHandle {
        val left = this
        return object : WatchHandle {
            override fun cancel() {
                left.cancel()
                right.cancel()
            }

            override fun close() {
                left.close()
                right.close()
            }

            override suspend fun join() {
                left.join()
                right.join()
            }
        }
    }
}
