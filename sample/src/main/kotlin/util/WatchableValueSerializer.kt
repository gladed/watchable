package util

import io.gladed.watchable.WatchableValue
import io.gladed.watchable.watchableValueOf
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.SerialClassDescImpl

@Serializer(forClass = WatchableValue::class)
class WatchableValueSerializer<T : Any>(private val dataSerializer: KSerializer<T>) : KSerializer<WatchableValue<T>> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("WatchableValue") {
        init {
            addElement("value")
        }
    }

    override fun serialize(encoder: Encoder, obj: WatchableValue<T>) {
        with(encoder.beginStructure(descriptor)) {
            encodeSerializableElement(descriptor, 0, dataSerializer, obj.value)
            endStructure(descriptor)
        }
    }

    override fun deserialize(decoder: Decoder): WatchableValue<T> =
        watchableValueOf(with(decoder.beginStructure(descriptor)) {
            val index = decodeElementIndex(descriptor)
            decodeSerializableElement(descriptor, index, dataSerializer).also {
                endStructure(descriptor)
            }
        })
}
