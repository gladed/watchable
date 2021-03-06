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

import io.gladed.watchable.WatchableList
import io.gladed.watchable.toWatchableList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = WatchableList::class)
class WatchableListSerializer<T : Any>(valueSerializer: KSerializer<T>) : KSerializer<WatchableList<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WatchableList") {
        listSerialDescriptor(valueSerializer.descriptor)
    }

    private val listSerializer = ListSerializer(valueSerializer)

    override fun serialize(encoder: Encoder, value: WatchableList<T>) {
        listSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): WatchableList<T> =
        listSerializer.deserialize(decoder).toWatchableList()
}
