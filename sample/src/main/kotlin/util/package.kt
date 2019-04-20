package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import store.Inflater

/** Convert this [KSerializer] to an [Inflater] of [String] and [T] */
fun <T : Any> KSerializer<T>.toInflater() = object : Inflater<String, T> {
    override fun String.inflate() =  Json.parse(this@toInflater, this)
    override fun T.deflate(): String = Json.stringify(this@toInflater, this)
}