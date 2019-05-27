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

import rest.Bird
import rest.CHIRP_PATH
import rest.Chirp
import rest.ChirpPage
import rest.ChirpReact
import rest.CreateBird
import rest.CreateChirp
import rest.Home
import rest.REACT_PATH
import util.KotlinSerializationConverter

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

fun model.Bird.toRestBird() = Bird(
    self = Bird.idToPath(id),
    name = name.value,
    following = following.map { Bird.idToPath(id) },
    chirps = "${Bird.idToPath(id)}$CHIRP_PATH"
)

fun model.Chirp.toRestChirp() = Chirp(
    self = Chirp.idToPath(id),
    from = Bird.idToPath(from),
    text = text,
    reactions = reactions.mapKeys { Bird.idToPath(it.key) },
    react = "${Chirp.idToPath(id)}$REACT_PATH"
)
