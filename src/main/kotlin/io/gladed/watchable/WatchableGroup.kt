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
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A group of [Watchable] objects that can be watched for any change.
 */
class WatchableGroup(
    private val watchables: List<Watchable<out Any, out Change<Any>>>
) : Watchable<List<Watchable<out Any, out Change<Any>>>, GroupChange> {

    override val value: List<Watchable<out Any, out Change<Any>>> = watchables

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun batch(
        scope: CoroutineScope,
        minPeriod: Long,
        consumer: suspend (List<GroupChange>) -> Unit
    ) = scope.subscription { closeMutex ->
        // Start watching other subscriptions, delivering their changes here.
        val subscriptions = watchables.map { watchable ->
            watchable.batch(scope) { changes ->
                consumer(changes.map { GroupChange(watchable, it) })
            }
        }.toMutableList()

        closeMutex.lock()
        subscriptions.forEach { it.closeAndJoin() }
    }
}
