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

import external.Adapter
import io.gladed.watchable.util.Cannot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CachingHeaders
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import logic.Logic
import org.slf4j.event.Level
import rest.Serializers
import util.KotlinSerializationConverter
import java.io.File

const val SAMPLE_PORT = 9090
const val LOCAL_HOST = "127.0.0.1"

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

@Suppress("unused") // Invoked externally
object Main {
    fun setup() {
        embeddedServer(Netty, port = SAMPLE_PORT, host = LOCAL_HOST, watchPaths = listOf("build/classes"),
            module = Application::main)
            .start(wait = true)
    }
}

fun Application.main() {
    val dataDir = File(".data")
    val logic = Adapter.createLogic(coroutineContext, dataDir)
    bind(logic)
}

fun Application.bind(logic: Logic) {
    val apiRoutes = ApiRoutes(logic)
    val htmlRoutes = HtmlRoutes(environment, logic)
    val currentDir = File(".").absoluteFile
    environment.log.info("Current directory: $currentDir")

    install(ContentNegotiation) {
        register(ContentType.Application.Json, KotlinSerializationConverter()) {
            addAll(Serializers.all.mapKeys { it.key.java })
        }
    }

    install(StatusPages) {
        exception<Cannot> { cause ->
            call.respond(HttpStatusCode.BadRequest, "cannot ${cause.message}")
            environment.log.warn("Couldn't", cause)
        }
    }

    install(CachingHeaders) {
        options {
            // ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
            CachingOptions(CacheControl.NoCache(CacheControl.Visibility.Public))
        }
    }

    install(CallLogging) {
        level = Level.INFO
    }

    routing {
        route("api") {
            apiRoutes.apply { routes() }
        }
        htmlRoutes.apply { routes() }

        static("/static") {
            resources("static")
        }
    }
}
