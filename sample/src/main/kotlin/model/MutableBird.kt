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

import io.gladed.watchable.Change
import io.gladed.watchable.Watchable
import io.gladed.watchable.WatchableList
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.group
import io.gladed.watchable.store.Container
import io.gladed.watchable.store.Transformer
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.watchableListOf
import kotlinx.serialization.Serializable
import util.WatchableListSerializer
import util.WatchableValueSerializer
import java.util.UUID

/**
 * Mutable form of [Bird].
 *
 * Note: this is kept distinct from [Bird] so that the holder can modify it only when
 * held. We can also add other features while keeping a clean core data model.
 */
@Serializable
data class MutableBird(
    val id: String = UUID.randomUUID().toString(),
    @Serializable(with = WatchableValueSerializer::class)
    val name: WatchableValue<String>,
    @Serializable(with = WatchableListSerializer::class)
    val following: WatchableList<String> = watchableListOf()
) : Container {
    override val watchables: Watchable<Change> = group(name, following)

    constructor(bird: Bird) : this(bird.id, bird.name.toWatchableValue(), bird.following.toWatchableList())

    fun toBird() = Bird(id, name.value, following.toList())

    companion object : Transformer<Bird, MutableBird> {
        override fun toTarget(value: Bird) = MutableBird(value)
        override fun fromTarget(value: MutableBird) = value.toBird()
    }
}
