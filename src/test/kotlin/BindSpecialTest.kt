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

import io.gladed.watchable.ListChange
import io.gladed.watchable.bind
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableValueOf
import org.junit.Assert.assertEquals
import org.junit.Test

class BindSpecialTest {
    @Test fun specialBinding() = runTest {
        // Show how we can transform data types into each other
        val origin = watchableListOf(4, 5)
        val dest = watchableValueOf(0)

        bind(dest, origin) {
            when(it) {
                is ListChange.Initial -> value = it.list.size
                is ListChange.Insert -> value += it.insert.size
                is ListChange.Remove -> value -= 1
                is ListChange.Replace -> { }
            }
        }

        origin {
            add(4)
            set(1, 6)
            remove(4)
        }

        // The size ends up 2
        triggerActions()
        assertEquals(2, dest.value)
    }
}
