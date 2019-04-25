package store

import java.util.UUID

data class Thing(val id: String = UUID.randomUUID().toString(), val value: Int)
