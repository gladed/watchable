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

package api

import io.gladed.watchable.toWatchableValue
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import logic.Logic
import model.Bird

@UseExperimental(FlowPreview::class)
class Routes(private val logic: Logic) {

    fun Routing.install() {
        get("/") {
            respond {
                Home(someBirds = birds.keys().take(SHORT_LIST_COUNT).map { api.Bird.keyToPath(it) }.toList())
            }
        }
        route(BIRD_PATH) { birdRoutes() }
        route(CHIRP_PATH) { chirpRoutes() }
    }

    /** Respond with result. */
    private suspend inline fun <Tx : Any> PipelineContext<Unit, ApplicationCall>.respond(
        crossinline func: suspend Logic.Scoped.() -> Tx
    ) = coroutineScope {
        call.respond(logic.scoped(this).func())
    }

    /** Parse incoming data, process it, and respond with result. */
    private suspend inline fun <reified Rx : Any, Tx : Any> PipelineContext<Unit, ApplicationCall>.process(
        crossinline func: suspend Logic.Scoped.(Rx) -> Tx
    ) = respond {
        func(call.receive())
    }

    private fun Route.chirpRoutes() {
        get("{chirpId}") {
            respond {
                chirps.get(call.parameters["chirpId"]!!)
            }
        }

        post("{chirpId}$REACT_PATH/{birdId}") {
            process { reaction: ChirpReaction ->
                val bird = birds.get(call.parameters["birdId"]!!)
                val chirp = chirps.get(call.parameters["chirpId"]!!)
                chirp.reactions {
                    if (reaction.reaction == null) {
                        remove(bird.id)
                    } else {
                        put(bird.id, reaction.reaction)
                    }
                }
                chirp
            }
        }
    }

    private fun Route.birdRoutes() {
        post {
            process { birdRequest: CreateBird ->
                // Create a new bird with the specified name
                val bird = Bird(name = birdRequest.name.toWatchableValue())
                birds.put(bird.id, bird)
                Bird(bird)
            }
        }

        get("{birdId}$CHIRP_PATH") {
            respond {
                val bird = birds.get(call.parameters["birdId"]!!)
                ChirpPage(logic.ops.chirpsForBird(bird.id)
                    .take(SHORT_LIST_COUNT)
                    .map { Chirp(chirps.get(it)) }
                    .toList())
            }
        }

        post("{birdId}$CHIRP_PATH") {
            process { chirpRequest: CreateChirp ->
                // Create a new chirp
                val bird = birds.get(call.parameters["birdId"]!!)
                val chirp = model.Chirp(from = bird.id, text = chirpRequest.text)
                chirps.put(chirp.id, chirp)
                Chirp(chirp)
            }
        }

        get("{birdId}") {
            respond {
                Bird(birds.get(call.parameters["birdId"]!!))
            }
        }
    }
    companion object {
        const val SHORT_LIST_COUNT = 10
    }
}
