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
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import model.Bird
import model.MutableBird
import model.toMutable
import util.KotlinSerializationConverter
import util.wrap
import java.io.File

fun main() = Main().go()

@Serializable
data class Home(val birdsUrl: String = "/bird", val someBirdUrls: List<String>)

@Serializable
data class CreateBird(val name: String)

@UseExperimental(FlowPreview::class)
class Main : CoroutineScope {

    override val coroutineContext = Dispatchers.Default + Job()
    private val logic = Adapter.createLogic(coroutineContext, File(".data"))

    fun go() {
        embeddedServer(Netty, SAMPLE_PORT) {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, KotlinSerializationConverter()) {
                    add(Home.serializer())
                    add(CreateBird.serializer())
                    add(Bird.serializer())
                    add(Bird.serializer().wrap(MutableBird))
                }
            }

            routing {
                get("/") {
                    call.respond(Home(someBirdUrls = logic.birds.back.keys()
                        .take(SHORT_LIST_COUNT).toList().map { "/bird/$it" }))
                }
                route("bird") { birdRoutes() }
            }
        }.start(wait = true)
    }

    private fun Route.birdRoutes() {
        post {
            // Create a new bird with the specified name
            val birdRequest = call.receive<CreateBird>()
            val bird = Bird(name = birdRequest.name).toMutable()

            coroutineScope {
                logic.birds.create(this).put(bird.id, bird)
            }

            call.respond(bird)
        }

        get("{id}") {
            call.respond(coroutineScope {
                logic.birds.create(this).get(call.parameters["id"]!!)
            })
        }
    }

    companion object {
        const val SAMPLE_PORT = 8080
        const val SHORT_LIST_COUNT = 10
    }
}
