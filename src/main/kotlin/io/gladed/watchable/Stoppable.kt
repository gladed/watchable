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

/** An ongoing operation that can be cancelled or stopped. */
interface Stoppable {
    /** Immediately stop. Repeated invocations have no effect. */
    fun cancel()

    /**
     * Gracefully stop, suspending if necessary to allow outstanding operations to complete.
     * Repeated invocations have no effect.
     */
    suspend fun stop()
}