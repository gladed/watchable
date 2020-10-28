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

import io.gladed.watchable.WatchableValue
import io.gladed.watchable.toWatchableValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.setSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = WatchableValue::class)
class WatchableValueSerializer<T : Any>(private val dataSerializer: KSerializer<T>) : KSerializer<WatchableValue<T>> {
    override val descriptor =
        buildClassSerialDescriptor("WatchableValue<${dataSerializer.descriptor.serialName}>") {
            setSerialDescriptor(dataSerializer.descriptor)
        }

    override fun serialize(encoder: Encoder, value: WatchableValue<T>) {
        dataSerializer.serialize(encoder, value.value)
    }

    override fun deserialize(decoder: Decoder): WatchableValue<T> =
        dataSerializer.deserialize(decoder).toWatchableValue()
}
