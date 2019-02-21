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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * A temporary [CoroutineScope] which runs on [Dispatchers.Main]. This scope can be independently closed,
 * causing all jobs launched within the scope to be cancelled.
 *
 * NOTE: IO or other blocking calls should be executed on a different dispatcher, e.g.:
 * `withContext(Dispatchers.IO)`.
 */
class LocalScope(private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : CoroutineScope, AutoCloseable {

    /** Cancellable parent job for this context. */
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    /** Cancel all jobs launched in the scope. */
    override fun close() {
        job.cancel()
    }
}
