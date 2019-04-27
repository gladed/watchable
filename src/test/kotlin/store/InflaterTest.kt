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

package store

import io.gladed.watchable.store.Inflater
import org.junit.Assert.assertEquals
import org.junit.Test

class InflaterTest {
    private val stringToInt = object : Inflater<String, Int> {
        override fun inflate(value: String): Int = value.toInt()
        override fun deflate(value: Int): String = value.toString()
    }

    private val intToString = object : Inflater<Int, String> {
        override fun inflate(value: Int): String = value.toString()
        override fun deflate(value: String): Int = value.toInt()
    }

    @Test fun cycle() {
        assertEquals(5, (intToString + stringToInt).inflate(5))
    }
}
