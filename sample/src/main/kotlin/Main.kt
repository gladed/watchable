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
import io.gladed.watchable.toWatchableValue
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import logic.Logic
import model.Bird
import model.Chirp
import org.slf4j.event.Level
import util.KotlinSerializationConverter
import java.io.File

fun main() = Main().go()

@Serializable
data class Home(val birdsUrl: String = "/bird", val someBirds: List<String>)

@Serializable
data class CreateBird(val name: String)

@Serializable
data class CreateChirp(val text: String)

@Serializable
data class ReactToChirp(val reaction: String?)

@Serializable
data class ChirpPage(val chirps: List<Chirp>)

@UseExperimental(FlowPreview::class)
class Main : CoroutineScope {

    override val coroutineContext = Dispatchers.Default + Job()
    private val logic = Adapter.createLogic(coroutineContext, File(".data"))

    // Note: This could be a feature
    private suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.withLogic(
        crossinline func: suspend Logic.Scoped.() -> T
    ) = coroutineScope {
        call.respond(logic.scoped(this).func())
    }

    fun go() {
        embeddedServer(Netty, SAMPLE_PORT) {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, KotlinSerializationConverter()) {
                    add(Home.serializer())
                    add(CreateBird.serializer())
                    add(CreateChirp.serializer())
                    add(ReactToChirp.serializer())
                    add(ChirpPage.serializer())
                    add(Bird.serializer())
                    add(Chirp.serializer())
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

            routing {
                get("/") {
                    withLogic {
                        Home(someBirds = birds.keys().take(SHORT_LIST_COUNT).toList().map { "/bird/$it" })
                    }
                }
                route("bird") { birdRoutes() }
                route("chirp") { chirpRoutes() }
            }
        }.start(wait = true)
    }

    private fun Route.chirpRoutes() {
        get("{chirpId}") {
            withLogic {
                chirps.get(call.parameters["chirpId"]!!)
            }
        }
    }

    private fun Route.birdRoutes() {
        post {
            withLogic {
                // Create a new bird with the specified name
                val birdRequest = call.receive<CreateBird>()
                val bird = Bird(name = birdRequest.name.toWatchableValue())
                birds.put(bird.id, bird)
                bird
            }
        }

        get("{birdId}/chirp") {
            withLogic {
                val bird = birds.get(call.parameters["birdId"]!!)
                ChirpPage(logic.ops.chirpsForBird(bird.id)
                    .take(SHORT_LIST_COUNT)
                    .map { chirps.get(it) }
                    .toList())
            }
        }
        post("{birdId}/chirp") {
            withLogic {
                // Create a new chirp
                val chirpRequest = call.receive<CreateChirp>()
                val bird = birds.get(call.parameters["birdId"]!!)
                Chirp(from = bird.id, text = chirpRequest.text).also { chirp ->
                    chirps.put(chirp.id, chirp)
                }
            }
        }

        post("{birdId}/chirp") {
            withLogic {
                // Create a new chirp
                val chirpRequest = call.receive<CreateChirp>()
                val bird = birds.get(call.parameters["birdId"]!!)
                Chirp(from = bird.id, text = chirpRequest.text).also { chirp ->
                    chirps.put(chirp.id, chirp)
                }
            }
        }

        get("{birdId}") {
            withLogic {
                birds.get(call.parameters["birdId"]!!)
            }
        }

        post("{birdId}/reactToChirp/{chirpId}") {
            withLogic {
                val reaction = call.receive<ReactToChirp>().reaction
                val bird = birds.get(call.parameters["birdId"]!!)
                val chirp = chirps.get(call.parameters["chirpId"]!!)
                chirp.reactions {
                    if (reaction == null) {
                        remove(bird.id)
                    } else {
                        put(bird.id, reaction)
                    }
                }
                chirp
            }
        }
    }

    companion object {
        const val SAMPLE_PORT = 8080
        const val SHORT_LIST_COUNT = 10
    }
}
