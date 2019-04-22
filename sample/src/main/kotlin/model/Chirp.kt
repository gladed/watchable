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
    /** A unique identifier for this person. */
    val id: String = UUID.randomUUID().toString(),

    /** ID of originating [Bird]. */
    val from: String,

    /** Time of sending. */
    val sentAt: LocalDateTime = LocalDateTime.now(),

    /** Text sent. */
    val text: String
)
