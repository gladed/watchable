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
import kotlinx.coroutines.Job
import kotlinx.coroutines.selects.whileSelect

/**
 * A group of [Watchable] objects that can be watched for any change.
 */
class WatchableGroup(
    private val watchables: List<Watchable<out Any, out Change<Any>>>
) : Watchable<List<Watchable<out Any, out Change<Any>>>, GroupChange> {

    override val value: List<Watchable<out Any, out Change<Any>>> = watchables

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun subscribe(scope: CoroutineScope): Subscription<GroupChange> =
        object : SubscriptionBase<GroupChange>() {
            override val daemon: Job = scope.daemon {
                // Watch all subscriptions until they are all closed
                val subscriptions = watchables.map { it to it.subscribe(scope) }.toMap()

                // Clean up subscription on exit
                coroutineContext[Job]?.invokeOnCompletion {
                    subscriptions.values.forEach { it.cancel() }
                }
                val remaining = subscriptions.toMutableMap()

                whileSelect {
                    remaining.forEach { (watchable, sub) ->
                        sub.onReceiveOrNull { changes ->
                            if (changes == null) {
                                remaining -= watchable
                                // exit daemon if no remaining subscriptions
                                remaining.isNotEmpty()
                            } else {
                                send(compile(changes, sub).map { GroupChange(watchable, it) })
                                true
                            }
                        }
                    }
                    daemonMonitor.onJoin { false }
                }
            }
        }
}
