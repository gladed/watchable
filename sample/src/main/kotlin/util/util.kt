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

package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import io.gladed.watchable.store.Inflater
import io.gladed.watchable.store.Store
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.UnstableDefault

/** Convert this [KSerializer] to an [Inflater] of [String] and [T] */
@UseExperimental(UnstableDefault::class)
fun <T : Any> KSerializer<T>.toInflater() = object : Inflater<String, T> {
    override fun inflate(value: String) = Json.parse(this@toInflater, value)
    override fun deflate(value: T): String = Json.stringify(this@toInflater, value)
}

fun <T : Any> Store<String>.inflate(serializer: KSerializer<T>): Store<T> = inflate(serializer.toInflater())

/** Return a new serializer from this [T] serializer which automatically inflates/deflates to [U]. */
fun <T : Any, U : Any> KSerializer<T>.wrap(inflater: Inflater<T, U>): KSerializer<U> =
    object : KSerializer<U> {
        override val descriptor: SerialDescriptor = this@wrap.descriptor

        override fun deserialize(decoder: Decoder): U = inflater.inflate(this@wrap.deserialize(decoder))

        override fun serialize(encoder: Encoder, obj: U) {
            this@wrap.serialize(encoder, inflater.deflate(obj))
        }
    }

/** Add both native and inflated serializers. */
inline fun <reified T : Any, reified U : Any>KotlinSerializationConverter.add(
    serializer: KSerializer<T>,
    inflater: Inflater<T, U>) {
    add(serializer)
    add(serializer.wrap(inflater))
}
