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

package util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import io.gladed.watchable.store.Transformer
import io.gladed.watchable.store.Store
import io.gladed.watchable.store.transform
import kotlinx.serialization.UnstableDefault

/** Convert this [KSerializer] to an [Transformer] of [String] and [T] */
@OptIn(UnstableDefault::class)
fun <T : Any> KSerializer<T>.toInflator() = object : Transformer<String, T> {
    @Suppress("DEPRECATION")
    override fun toTarget(value: String) = Json.nonstrict.parse(this@toInflator, value)
    override fun fromTarget(value: T): String = Json.stringify(this@toInflator, value)
}

fun <T : Any> Store<String>.serialize(serializer: KSerializer<T>): Store<T> = transform(serializer.toInflator())
