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

package rest

import kotlinx.serialization.Serializable

// Data objects for interacting with REST clients.

const val BIRD_PATH = "/bird"
const val CHIRP_PATH = "/chirp"
const val REACT_PATH = "/react"

object Serializers {
    val all = mapOf(
        Home::class to Home.serializer(),
        Bird::class to Bird.serializer(),
        Chirp::class to Chirp.serializer(),
        CreateBird::class to CreateBird.serializer(),
        CreateChirp::class to CreateChirp.serializer(),
        ChirpReact::class to ChirpReact.serializer(),
        ChirpPage::class to ChirpPage.serializer())
}

@Serializable
data class Home(
    val birds: String = BIRD_PATH,
    val chirps: String = CHIRP_PATH,
    val someBirds: List<String>
)

@Serializable
data class Bird(
    val self: String,
    val name: String,
    val following: List<String>,
    val chirps: String
) {
    companion object {
        fun idToPath(id: String) = "$BIRD_PATH/$id"
    }
}

@Serializable
data class Chirp(
    val self: String,
    val from: String,
    val text: String,
    // Map of reactions from bird ids to reaction texts
    val reactions: Map<String, String>,
    val react: String
) {
    companion object {
        fun idToPath(id: String) = "$CHIRP_PATH/$id"
    }
}

@Serializable
data class CreateBird(val name: String)

@Serializable
data class CreateChirp(val text: String)

@Serializable
data class ChirpReact(val from: String, val reaction: String?)

@Serializable
data class ChirpPage(
    val chirps: List<Chirp>,
    val nextPage: String? = null
)

