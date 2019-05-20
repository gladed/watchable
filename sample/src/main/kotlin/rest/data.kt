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

import util.KotlinSerializationConverter
import kotlinx.serialization.Serializable

const val BIRD_PATH = "/bird"
const val CHIRP_PATH = "/chirp"
const val REACT_PATH = "/react"

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
    constructor(bird: model.Bird) : this(
        self = keyToPath(bird.id),
        name = bird.name.value,
        following = bird.following.map { keyToPath(it) },
        chirps = "${keyToPath(bird.id)}$CHIRP_PATH"
    )
    companion object {
        fun keyToPath(key: String) = "$BIRD_PATH/$key"
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
    constructor(chirp: model.Chirp) : this(
        self = "$CHIRP_PATH/${chirp.id}",
        from = Bird.keyToPath(chirp.from),
        text = chirp.text,
        reactions = chirp.reactions.mapKeys { "$BIRD_PATH/${it.key}" },
        react = "$CHIRP_PATH/${chirp.id}$REACT_PATH"
    )
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

/** Add all serializers for types exposed here. */
fun KotlinSerializationConverter.registerSerializers() {
    add(Home.serializer())
    add(Bird.serializer())
    add(Chirp.serializer())
    add(CreateBird.serializer())
    add(CreateChirp.serializer())
    add(ChirpReact.serializer())
    add(ChirpPage.serializer())
}
