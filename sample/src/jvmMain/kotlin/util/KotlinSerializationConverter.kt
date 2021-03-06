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

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

/** Ktor content converter for explicitly registered @Serializable classes. */
@OptIn(KtorExperimentalAPI::class)
class KotlinSerializationConverter : ContentConverter {
    var serializers = mutableMapOf<Class<*>, KSerializer<*>>()

    override suspend fun convertForReceive(
        context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>
    ): Any? =
        serializers[context.subject.type.javaObjectType]?.let {
            val bytes = context.subject.value as? ByteReadChannel ?: return null
            @Suppress("DEPRECATION")
            Json.decodeFromString(it, bytes.readRemaining(MAX_REQUEST_SIZE, 0).readText())
        }

    override suspend fun convertForSend(
        context: PipelineContext<Any, ApplicationCall>,
        contentType: ContentType,
        value: Any
    ): Any? =
        serializers[value.javaClass]?.let { serializer ->
            @Suppress("UNCHECKED_CAST") // Necessary to extract known-good serializer
            TextContent(Json.encodeToString(serializer as SerializationStrategy<Any>, value),
                contentType.withCharset(context.call.suitableCharset()))
        }

    /** Install a bunch of serializers for the specified type. */
    fun addAll(items: Map<Class<*>, KSerializer<*>>) {
        serializers.putAll(items)
    }

    companion object {
        const val MAX_REQUEST_SIZE = 1024 * 10L
    }
}
