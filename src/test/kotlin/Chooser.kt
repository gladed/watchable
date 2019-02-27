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

import kotlin.random.Random

/** A class that makes pseudorandom choices. */
class Chooser(seed: Int) {
    private val random = Random(seed)
    operator fun invoke(max: Int): Int = random.nextInt(max)
    operator fun <T> invoke(collection: Collection<T>): T? =
        if (collection.isEmpty()) null else {
            ((collection as? List<T>) ?: collection.toList())[invoke(collection.size)]
        }
}