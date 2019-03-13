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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** A scope that keeps a reference to an object and cancels itself as soon as tracked job(s) complete. */
class TrackingScopeTest<T : Any>(dispatcher: CoroutineDispatcher, trackedJob: Job, thing: T) : CoroutineScope {
    override val coroutineContext = dispatcher + Job()
    private val mutex = Mutex()
    private val otherScopes = mutableListOf(trackedJob)

    var value: T? = thing
        private set(value) { field = value }

    init {
        trackedJob.invokeOnCompletion { untrack(trackedJob) }
        coroutineContext[Job]?.invokeOnCompletion { value = null }
    }

    /** Track a new job. */
    suspend fun track(job: Job) {
        if (!isActive) throw IllegalStateException("Cannot track new jobs")
        mutex.withLock {
            otherScopes.add(job)
        }

        job.invokeOnCompletion {
            untrack(job)
        }
    }

    private fun untrack(job: Job) {
        if (isActive) {
            launch {
                if (mutex.withLock {
                        otherScopes.remove(job)
                        otherScopes.isEmpty()
                    }) coroutineContext[Job]?.cancel()
            }
        }
    }
}
