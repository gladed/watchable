package util

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("LocalDateTimeSerializer")

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneOffset.UTC)

    override fun serialize(encoder: Encoder, obj: LocalDateTime) {
        encoder.encodeString(formatter.format(obj.atOffset(ZoneOffset.UTC)))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
