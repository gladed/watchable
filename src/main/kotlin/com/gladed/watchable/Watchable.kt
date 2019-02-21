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

package com.gladed.watchable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive

/**
 * An object wrapping type [T] which can be watched for changes of type [C].
 */
interface Watchable<T, C: Change<T>> : CoroutineScope {
    /**
     * Deliver changes to [block] using [scope] until it terminates or until the returned [Job] is cancelled.
     * Note that calling [watch] will normally result in an immediate call to [block], announcing the initial value.
     */
    fun watch(
        /** The scope within which [block] should be received. Defaults to this object's scope. */
        scope: CoroutineScope = this,
        /** The block to invoke within [scope] whenever a change occurs. */
        block: (C) -> Unit
    ): Job

    /** Return true if this [Watchable]'s scope is still active, allowing new [watch] requests to succeed. */
    fun isActive() = coroutineContext.isActive
}
