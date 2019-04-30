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

/** Mutable form of [Bird]. */
data class MutableBird(
    val id: String = UUID.randomUUID().toString(),
    val name: WatchableValue<String>,
    val following: WatchableList<String> = watchableListOf()
) {
    val watchables = group(name, following)

    companion object : Inflater<Bird, MutableBird> {
        override fun inflate(value: Bird) = with(value) {
            MutableBird(id, name.toWatchableValue(), following.toWatchableList())
        }
        override fun deflate(value: MutableBird) = with(value) {
            Bird(id, name.value, following.toList())
        }
    }

    fun toImmutable() = deflate(this)
}

fun Bird.toMutable() = MutableBird.inflate(this)
