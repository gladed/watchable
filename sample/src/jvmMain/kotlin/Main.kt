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
import io.gladed.watchable.store.cannot
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
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.input
import kotlinx.html.title
import logic.Logic
import model.Bird
import model.Chirp
import org.slf4j.event.Level
import rest.Serializers
import util.KotlinSerializationConverter
import java.io.File

const val SAMPLE_PORT = 9090
const val LOCAL_HOST = "127.0.0.1"

fun main() { Main.setup() }

object Main {
    fun setup() {
        embeddedServer(Netty, port = SAMPLE_PORT, host = LOCAL_HOST, watchPaths = listOf("build/classes"),
            module = Application::setup)
            .start(wait = true)
    }

    fun setup(app: Application, logic: Logic) = app.setup(logic)
}

fun Application.setup() {
    val dataDir = File(".data")
    val logic = Adapter.createLogic(coroutineContext, dataDir)
    setup(logic)
}

@UseExperimental(FlowPreview::class)
fun Application.setup(logic: Logic) {
    val routes = Routes(logic)
    val currentDir = File(".").absoluteFile
    environment.log.info("Current directory: $currentDir")

    val webDir = listOf(
            "web",
            "../src/jsMain/web",
            "src/jsMain/web"
        ).map {
            File(currentDir, it)
        }.firstOrNull { it.isDirectory }?.absoluteFile ?: error("Can't find 'web' folder for this sample")

    suspend fun <T> withLogic(func: suspend Logic.Scoped.() -> T): T =
        coroutineScope { with(logic.scoped(this)) { func() } }

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

    install(CallLogging) {
        level = Level.INFO
    }


    routing {
        with(routes) {
            route("api") {
                installRoutes()
            }
        }
        get("/") {
            val topBirds = withLogic {
                birds.keys().take(10).map { birds.get(it) }.toList()
            }
            call.respondHtml {
                head {
                    title("Birds!")
                }
                body {
                    +"${hello()} (from Ktor)."
//                    div {
//                        id = "js-response"
//                        +"Loading..."
//                    }
//                    script(src = "/static/require.min.js") {
//                    }
//                    script {
//                        unsafe {
//                            +"require.config({baseUrl: '/static'});\n"
//                            +"require(['/static/sample.js'], function(js) { js.helloWorld('Hi'); });\n"
//                        }
//                    }
                    h2 { +"Birds" }
                    topBirds.forEach { bird ->
                        div {
                            a(href="/bird/${bird.id}") { +bird.name.value }
                        }
                    }

                    form(action = "bird", method = FormMethod.post) {
                        +"Name: "
                        input(InputType.text, name = "name")
                        +" "
                        input(InputType.submit) {
                            value = "Create Bird"
                        }
                    }
                }
            }
        }

        // Create a new bird with a name
        post("/bird") {
            val parameters = call.receiveParameters()
            val name = parameters["name"]!!
            val bird = withLogic {
                Bird(name = name).also {
                    birds.put(it.id, it)
                }
            }
            call.respondRedirect("/bird/${bird.id}")
        }

        // Delete a bird by ID
        post("/bird/{birdId}/delete") {
            withLogic {
                birds.remove(call.parameters["birdId"]!!)
            }
            call.respondRedirect("/")
        }

        // Show a page about a bird
        get("/bird/{birdId}") {
            withLogic {
                birds.get(call.parameters["birdId"]!!).let { bird ->
                    val myChirps = ops.chirpsForBird(bird.id).take(10).map {
                        chirps.get(it)
                    }.toList()
                    val follows = bird.following.map { birds.get(it) }

                    call.respondHtml {
                        head {
                            title(bird.name.value)
                        }
                        body {
                            h1 { +bird.name.value }
                            form(action = "/bird/${bird.id}/delete", method = FormMethod.post) {
                                input(InputType.submit) {
                                    value = "Delete"
                                }
                            }

                            h2 { +"Chirps" }
                            myChirps.forEach {
                                div {
                                    +"At ${it.sentAt}, ${bird.name} chirped: ${it.text}"
                                    br
                                    +"Reactions: ${it.reactions.values.sorted()}"
                                    form(action = "/chirp/${it.id}/delete", method = FormMethod.post) {
                                        input(InputType.submit) {
                                            value = "Delete"
                                        }
                                    }
                                }
                            }

                            form(action = "/chirp", method = FormMethod.post) {
                                input(InputType.hidden, name = "bird") {
                                    value = bird.id
                                }
                                +"Text: "
                                input(InputType.text, name = "text")
                                +" "
                                input(InputType.submit) {
                                    value = "Chirp"
                                }
                            }

                            h2 { +"Follows" }
                            follows.forEach {
                                div {
                                    a(href = "/bird/${it.id}") { +it.name.value }
                                    form(action = "/bird/${bird.id}/unfollow", method = FormMethod.post) {
                                        input(InputType.hidden, name = "bird") {
                                            value = it.id
                                        }
                                        input(InputType.submit) {
                                            value = "Unfollow"
                                        }
                                    }
                                }
                            }
                            form(action = "/bird/${bird.id}/follow", method = FormMethod.post) {
                                +"Name: "
                                input(InputType.text, name = "name")
                                +" "
                                input(InputType.submit) {
                                    value = "Follow"
                                }
                            }
                        }
                    }
                }
            }
        }

        post("/bird/{birdId}/follow") {
            withLogic {
                val me = birds.get(call.parameters["birdId"]!!)
                val parameters = call.receiveParameters()
                val name = parameters["name"]!!
                val other = ops.birdsWithName(name).toList().sortedBy {
                    val otherName = it.name.value
                    when {
                        (otherName == name) -> 0
                        (otherName.toLowerCase() == name.toLowerCase()) -> 1
                        (otherName.contains(name)) -> 2
                        (name.contains(otherName)) -> 3
                        else -> 100
                    }
                }.firstOrNull() ?: cannot("find any bird by that name $name")
                me.following.add(other.id)
                call.respondRedirect("/bird/${me.id}")
            }
        }

        post("/bird/{birdId}/unfollow") {
            withLogic {
                val me = birds.get(call.parameters["birdId"]!!)
                val parameters = call.receiveParameters()
                me.following.remove(parameters["bird"]!!)
                call.respondRedirect("/bird/${me.id}")
            }
        }

        // Send a chirp to the world
        post("/chirp") {
            withLogic {
                val parameters = call.receiveParameters()
                val birdId = parameters["bird"]!!
                val text = parameters["text"]!!
                Chirp(text = text, from = birdId).also {
                    chirps.put(it.id, it)
                }
                call.respondRedirect("/bird/$birdId")
            }
        }

        // Oops I didn't mean to say that
        post("/chirp/{chirpId}/delete") {
            withLogic {
                val chirpId = call.parameters["chirpId"]!!
                val birdId = chirps.get(chirpId).from
                chirps.remove(chirpId)
                call.respondRedirect("/bird/$birdId")
            }
        }

        static("/static") {
            files(webDir)
        }
    }
}
