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
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.newSingleThreadContext

/**
 * An object wrapping type [T] which can be watched for changes of type [C].
 */
interface Watchable<T, C : Change<T>> : CoroutineScope {
    /**
     * Deliver changes to [block] using this [CoroutineScope] until it terminates or until the returned [Job] is
     * cancelled. Each change is processed to completion (e.g. [block] must return) before the next change is
     * delivered.
     *
     * Calling [watch] will immediately launch a call to [block] to inform it of the [Watchable]'s initial value.
     * Further calls will arrive if and when this [Watchable] changes.
     */
    fun CoroutineScope.watchBatches(
        /** The block to invoke within this [CoroutineScope] whenever a change occurs. */
        block: suspend (List<C>) -> Unit
    ): Job

    /**
     * Deliver changes to [block] using this [CoroutineScope] until it terminates or until the returned [Job] is
     * cancelled. Each change is processed to completion (e.g. [block] must return) before the next change is
     * delivered.
     *
     * Calling [watch] will immediately launch a call to [block] to inform it of the [Watchable]'s initial value.
     * Further calls will arrive if and when this [Watchable] changes.
     */
    fun CoroutineScope.watch(
        /** The block to invoke within this [CoroutineScope] whenever a change occurs. */
        block: suspend (C) -> Unit
    ): Job =
        watchBatches { changes ->
            changes.forEach { change -> block(change) }
        }

    /** Return true if this [Watchable]'s scope is still active, allowing new [watch] requests to succeed. */
    fun isActive() = coroutineContext.isActive

    companion object {
        @UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class)
        internal val writeContext = newSingleThreadContext("watchables")
    }
}
