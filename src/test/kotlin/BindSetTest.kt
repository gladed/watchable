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

import com.gladed.watchable.watchableSetOf
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.lang.IllegalStateException

class BindSetTest {
    @Test fun bind() {
        runThenCancel {
            val origin = watchableSetOf(5)
            val dest = watchableSetOf(6)
            dest.bind(origin)
            yield()
            yield()
            assertEquals(setOf(5), dest)
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableSetOf(4, 5)
            val dest = watchableSetOf(6)
            dest.bind(origin)
            origin += listOf(8, 7)
            origin -= 5
            yield()
            yield()
            origin += listOf(9)
            origin -= 4
            yield()
            yield()
            // Order doesn't matter to sets
            assertEquals(setOf(7, 8, 9), dest)
            // But it matters to iterators
            assertEquals(8, dest.iterator().next())
        }
    }

    @Test fun badAdd() {
        try {
            runThenCancel {
                val origin = watchableSetOf(4, 5)
                val dest = watchableSetOf(6)
                dest.bind(origin)
                dest += 7
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun badRemove() {
        try {
            runThenCancel {
                val origin = watchableSetOf(4, 5)
                val dest = watchableSetOf(6)
                dest.bind(origin)
                yield()
                yield()
                dest -= 5
                fail("Modification should not have been permitted")
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbindThenChange() {
        runThenCancel {
            val origin = watchableSetOf(4, 5)
            val dest = watchableSetOf(6)
            dest.bind(origin)
            origin += listOf(8, 7)
            yield()
            yield()
            dest.unbind()
            origin -= 5
            yield()
            yield()
            assertEquals(setOf(4, 5, 7, 8), dest)
        }
    }
}