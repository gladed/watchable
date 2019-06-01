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
import io.gladed.watchable.store.Cannot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.script
import kotlinx.html.title
import kotlinx.html.unsafe
import org.slf4j.event.Level
import rest.Serializers
import util.KotlinSerializationConverter
import java.io.File

fun main() = Main(File(".data")).go()

@UseExperimental(FlowPreview::class)
class Main(dataDir: File) : CoroutineScope {

    override val coroutineContext = Dispatchers.Default + Job()
    private val logic = Adapter.createLogic(coroutineContext, dataDir)
    private val routes = Routes(logic)

    fun go() {
        embeddedServer(Netty, port = SAMPLE_PORT, host = LOCAL_HOST) {
            setup()
        }.start(wait = true)
    }

    fun Application.setup() {
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
            }
        }

        install(CallLogging) {
            level = Level.INFO
        }

        val webDir = listOf(
            "web",
            "../src/jsMain/web",
            "src/jsMain/web"
        ).map {
            File(currentDir, it)
        }.firstOrNull { it.isDirectory }?.absoluteFile ?: error("Can't find 'web' folder for this sample")

        routing {
            with(routes) {
                route("api") {
                    installRoutes()
                }
            }
            get("/") {
                call.respondHtml {
                    head {
                        title("Hello from Ktor!")
                    }
                    body {
                        +"${hello()} (from Ktor)."
                        div {
                            id = "js-response"
                            +"Loading..."
                        }
                        script(src = "/static/require.min.js") {
                        }
                        script {
                            unsafe {
                                +"require.config({baseUrl: '/static'});\n"
                                +"require(['/static/sample.js'], function(js) { js.helloWorld('Hi'); });\n"
                            }
                        }
                    }
                }
            }
            static("/static") {
                files(webDir)
            }
        }
    }

    companion object {
        const val SAMPLE_PORT = 9090
        const val LOCAL_HOST = "127.0.0.1"
    }
}
