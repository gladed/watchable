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
import io.ktor.features.CachingHeaders
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.html.respondHtml
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.head
import kotlinx.html.input
import kotlinx.html.p
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

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

object Main {
    fun setup() {
        Sample().checkMe()
        Platform.name
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

@UseExperimental(FlowPreview::class)
fun Application.bind(logic: Logic) {
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
        withContext(Dispatchers.Default + Job()) {
            logic.scoped(this).func()
        }

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
            //                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
            CachingOptions(CacheControl.NoCache(CacheControl.Visibility.Public))
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
                birds.keys().take(10).map {
                    birds.get(it) }.toList()
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
                    h1 { +"Birds" }
                    p { +"Some birds:" }
                    topBirds.forEach { bird ->
                        div {
                            a(href="/bird/${bird.id}") { +bird.name.value }
                        }
                    }

                    form(action = "bird", method = FormMethod.post) {
                        h3 { +"Create a bird" }
                        input(InputType.text, name = "name") {
                            placeholder = "name of new bird"
                        }
                        +" "
                        input(InputType.submit) {
                            value = "Create"
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

        // $a bird by ID
        post("/bird/{birdId}/delete") {
            val done = withLogic {
                birds.remove(call.parameters["birdId"]!!)
                coroutineContext[Job]
            }
            environment.log.info("Done $done")
            call.respondRedirect("/")
        }

        // Show a page about a bird
        get("/bird/{birdId}") {
            withLogic {
                birds.get(call.parameters["birdId"]!!).let { bird ->
                    val related = ops.relatedChirps(bird.id).take(10).map { chirpKey ->
                        chirps.get(chirpKey).let { chirp -> chirp to birds.get(chirp.from) }
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
                                    value = "Delete ${bird.name}"
                                }
                            }

                            h2 { +"Chirps" }
                            related.forEach { (chirp, from) ->
                                div {
                                    +"${chirp.sentAt}, ${from.name}: ${chirp.text}"
                                    br
                                    +"Reactions: ${chirp.reactions.values.sorted()}"
                                    if (chirp.from == bird.id) {
                                        form(action = "/chirp/${chirp.id}/delete", method = FormMethod.post) {
                                            input(InputType.submit) {
                                                value = "Delete chirp"
                                            }
                                        }
                                    }
                                }
                            }

                            form(action = "/chirp", method = FormMethod.post) {
                                h3 { +"Chirp something from ${bird.name.value}:" }
                                input(InputType.hidden, name = "bird") {
                                    value = bird.id
                                }
                                input(InputType.text, name = "text") {
                                    placeholder = "text to chirp"
                                }
                                +" "
                                input(InputType.submit) {
                                    value = "Chirp"
                                }
                            }

                            h2 { +"Follows" }
                            follows.forEach { following ->
                                div {
                                    a(href = "/bird/${following.id}") { +following.name.value }
                                    form(action = "/bird/${bird.id}/unfollow", method = FormMethod.post) {
                                        input(InputType.hidden, name = "bird") {
                                            value = following.id
                                        }
                                        input(InputType.submit) {
                                            value = "Unfollow ${following.name}"
                                        }
                                    }
                                }
                            }


                            form(action = "/bird/${bird.id}/follow", method = FormMethod.post) {
                                h3 { +"Follow a new bird:" }
                                input(InputType.text, name = "name") {
                                    placeholder = " name of bird to follow"
                                }
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
