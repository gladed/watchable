package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import store.Inflater

/** Convert this [KSerializer] to an [Inflater] of [String] and [T] */
fun <T : Any> KSerializer<T>.toInflater() = object : Inflater<String, T> {
    override fun inflate(value: String) = Json.parse(this@toInflater, value)
    override fun deflate(value: T): String = Json.stringify(this@toInflater, value)
}
