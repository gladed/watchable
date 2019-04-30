package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import io.gladed.watchable.store.Inflater
import io.gladed.watchable.store.Store
import kotlinx.serialization.UnstableDefault

/** Convert this [KSerializer] to an [Inflater] of [String] and [T] */
@UseExperimental(UnstableDefault::class)
fun <T : Any> KSerializer<T>.toInflater() = object : Inflater<String, T> {
    override fun inflate(value: String) = Json.parse(this@toInflater, value)
    override fun deflate(value: T): String = Json.stringify(this@toInflater, value)
}

fun <T : Any> Store<String>.inflate(serializer: KSerializer<T>): Store<T> = inflate(serializer.toInflater())
