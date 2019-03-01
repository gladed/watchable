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

import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class WatchableSetTest : CoroutineScope {

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext
    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxValue = 100

    private val mods = listOf<MutableSet<Int>.() -> Unit>(
        { remove(chooser(maxValue)) },
        { add(chooser(maxValue)) }
    )

    @Test fun changes() {
        runToEnd {
            val set = watchableSetOf(1, 2)
            val set2 = watchableSetOf<Int>()
            set2.bind(set)
            val set3 = set2.readOnly()
            assertThat(set.toString(), startsWith("WatchableSet("))
            assertThat(set3.toString(), startsWith("ReadOnlyWatchableSet("))
            (0 until 10000).map {
                launch {
                    set.use { chooser(mods)!!(this) }
                }
            }.joinAll()
            // Add something special
            set.use {
                add(maxValue + 1)
                log("Set size: $size") // coverage
            }
            set3.watch {
                if (set.get() == set3.get()) {
                    coroutineContext.cancel()
                }
            }
            delay(2000)
            assertEquals(set.get(), set3.get())
        }
    }
}
