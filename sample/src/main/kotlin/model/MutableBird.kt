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
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.toWatchableValue
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.store.Inflater
import java.util.UUID

/**
 * Mutable form of [Bird].
 *
 * Note: this is kept distinct from [Bird] so that the holder can modify it only when
 * held. We can also add other features while keeping a clean core data model.
 */
data class MutableBird(
    val id: String = UUID.randomUUID().toString(),
    val name: WatchableValue<String>,
    val following: WatchableList<String> = watchableListOf()
) {
    constructor(bird: Bird) : this(bird.id, bird.name.toWatchableValue(), bird.following.toWatchableList())

    fun toBird() = Bird(id, name.value, following.toList())

    val watchables = group(name, following)

    companion object : Inflater<Bird, MutableBird> {
        override fun inflate(value: Bird) = MutableBird(value)
        override fun deflate(value: MutableBird) = value.toBird()
    }
}
