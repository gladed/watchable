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
@file:UseSerializers(LocalDateTimeSerializer::class)
package model

import io.gladed.watchable.WatchableMap
import io.gladed.watchable.store.Inflater
import io.gladed.watchable.toWatchableMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import util.LocalDateTimeSerializer
import util.WatchableMapSerializer
import java.time.LocalDateTime

/** Mutable form of a [Chirp]. */
@Serializable
data class MutableChirp(
    val id: String,
    val from: String,
    val sentAt: LocalDateTime = LocalDateTime.now(),
    val text: String,
    @Serializable(with = WatchableMapSerializer::class)
    val reactions: WatchableMap<String, String>
) {
    constructor(chirp: Chirp) : this(
        chirp.id, chirp.from, chirp.sentAt, chirp.text, chirp.reactions.toWatchableMap())
    fun toChirp() = Chirp(id, from, sentAt, text, reactions.toMap())

    companion object : Inflater<Chirp, MutableChirp> {
        override fun inflate(value: Chirp) = MutableChirp(value)
        override fun deflate(value: MutableChirp) = value.toChirp()
    }
}
