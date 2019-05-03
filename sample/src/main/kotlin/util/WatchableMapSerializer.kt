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
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.toWatchableMap
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.internal.NamedMapClassDescriptor
import kotlinx.serialization.internal.StringSerializer

@Serializer(forClass = WatchableList::class)
class WatchableMapSerializer<T : Any>(
    stringSerializer: KSerializer<String>,
    valueSerializer: KSerializer<T>
) : KSerializer<WatchableMap<String, T>> {
    override val descriptor = NamedMapClassDescriptor("WatchableMap",
        stringSerializer.descriptor, valueSerializer.descriptor)

    private val mapSerializer = HashMapSerializer(StringSerializer, valueSerializer)

    override fun serialize(encoder: Encoder, obj: WatchableMap<String, T>) {
        mapSerializer.serialize(encoder, obj)
    }

    override fun deserialize(decoder: Decoder): WatchableMap<String, T> =
        mapSerializer.deserialize(decoder).toWatchableMap()
}