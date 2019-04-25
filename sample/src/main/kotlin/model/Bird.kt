package model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Bird(
    /** A unique identifier for this person. */
    val id: String = UUID.randomUUID().toString(),

    /** The name of this person. */
    val name: String,

    /** [Bird.id]s of [Bird]s this [Bird] is following. */
    val following: List<String> = listOf()
)
