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

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import util.LocalDateTimeSerializer
import java.time.LocalDateTime
import java.util.UUID

/** Something memorable sent out by a [Bird]. */
@Serializable
data class Chirp(
    val id: String = UUID.randomUUID().toString(),
    /** Originator of this [Chirp]/ */
    val from: String,
    /** Content of the chirp. */
    val text: String,
    /** Time of delivery. */
    val sentAt: LocalDateTime = LocalDateTime.now(),
    /** [Chirp] this is in reply to, if any. */
    val replyTo: String? = null
)
