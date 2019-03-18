package util

import io.gladed.watchable.WatchableList
import io.gladed.watchable.toWatchableList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.NamedListClassDescriptor

@Serializer(forClass = WatchableList::class)
class WatchableListSerializer<T : Any>(private val dataSerializer: KSerializer<T>) : KSerializer<WatchableList<T>> {
    override val descriptor: SerialDescriptor = NamedListClassDescriptor("WatchableValue", dataSerializer.descriptor)

    override fun serialize(encoder: Encoder, obj: WatchableList<T>) {
        with(encoder.beginStructure(descriptor)) {
            runBlocking { obj.value }.forEachIndexed { index, item ->
                encodeSerializableElement(descriptor, index, dataSerializer, item)
            }
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): WatchableList<T> =
        with(decoder.beginStructure(descriptor)) {
            val items = mutableListOf<T>()
            loop@while(true) {
                when(val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.READ_DONE -> break@loop
                    else -> items += decodeSerializableElement(descriptor, index, dataSerializer)
                }
            }
            items.toWatchableList().also {
                endStructure(descriptor)
            }
        }
}
