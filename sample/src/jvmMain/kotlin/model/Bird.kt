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

package model

import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.group
import io.gladed.watchable.store.Container
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.watchableListOf
import kotlinx.serialization.Serializable
import util.WatchableListSerializer
import util.WatchableValueSerializer
import java.util.UUID

/**
 * A thing that chirps.
 */
@Serializable
data class Bird(
    val id: String = UUID.randomUUID().toString(),

    @Serializable(with = WatchableValueSerializer::class)
    val name: WatchableValue<String>,

    @Serializable(with = WatchableListSerializer::class)
    val following: WatchableList<String> = watchableListOf()
) : Container {
    constructor(name: String) : this(name = name.toWatchableValue())

    override val watchables by lazy { group(name, following) }
}
