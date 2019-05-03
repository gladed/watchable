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
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.NamedListClassDescriptor

@Serializer(forClass = WatchableList::class)
class WatchableListSerializer<T : Any>(valueSerializer: KSerializer<T>) : KSerializer<WatchableList<T>> {
    override val descriptor: SerialDescriptor = NamedListClassDescriptor("WatchableValue", valueSerializer.descriptor)

    private val listSerializer = ArrayListSerializer(valueSerializer)

    override fun serialize(encoder: Encoder, obj: WatchableList<T>) {
        listSerializer.serialize(encoder, obj)
    }

    override fun deserialize(decoder: Decoder): WatchableList<T> =
        listSerializer.deserialize(decoder).toWatchableList()
}
