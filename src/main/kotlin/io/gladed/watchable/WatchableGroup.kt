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

/**
 * A group of [Watchable] objects that can be watched for any change, which arrives as a [GroupChange].
 * Can also be created with [group].
 */
class WatchableGroup(
    private val watchables: List<Watchable<*>>
) : Watchable<GroupChange> {

    override fun batch(
        scope: CoroutineScope,
        period: Long,
        func: suspend (List<GroupChange>) -> Unit
    ): Watcher {
        val watchers = watchables.map { watchable ->
            watchable.batch(scope) { changes ->
                func(changes.map { GroupChange(watchable, it) })
            }
        }.toMutableList()

        return object : Watcher {
            override suspend fun start() {
                watchers.forEach { it.start() }
            }

            override fun cancel() {
                watchers.forEach { it.cancel() }
                watchers.clear()
            }

            override suspend fun stop() {
                watchers.forEach { it.stop() }
                watchers.clear()
            }
        }
    }
}
