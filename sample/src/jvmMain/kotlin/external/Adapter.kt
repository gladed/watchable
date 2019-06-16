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

package external

import io.gladed.watchable.store.cached
import logic.Logic
import logic.Operations
import model.Bird
import model.Chirp
import util.serialize
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Construct real-world objects for use by the application. */
object Adapter {

    /** Create a [Logic] object based on a root directory. */
    fun createLogic(context: CoroutineContext, root: File): Logic {
        val birds = FileStore(root, "bird", JSON_SUFFIX).serialize(Bird.serializer()).cached(context)
        val chirps = FileStore(root, "chirp", JSON_SUFFIX).serialize(Chirp.serializer()).cached(context)
        return Logic(context, birds, chirps, Operations(chirps, birds))
    }

    private const val JSON_SUFFIX = "json"
}
