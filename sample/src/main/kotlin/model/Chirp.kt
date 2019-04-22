package model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Chirp(
    /** A unique identifier for this person. */
    val id: String = UUID.randomUUID().toString(),

    /** ID of originating [Bird]. */
    val from: String,

    /** Text sent. */
    val text: String
)
