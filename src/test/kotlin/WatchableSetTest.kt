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

import io.gladed.watchable.SetChange
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class WatchableSetTest : CoroutineScope {
    @Rule @JvmField val changes = ChangeWatcherRule<SetChange<Int>>()

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext
    private val chooser = Chooser(0)
    private val maxValue = 100

    private val mods = listOf<MutableSet<Int>.() -> Unit>(
        { remove(chooser(maxValue)) },
        { add(chooser(maxValue)) }
    )

    @Test fun changes() {
        CoroutineScope(coroutineContext + Job()).apply {
            runToEnd {
                iterateMutable(this@apply,
                    watchableSetOf(1, 2),
                    watchableSetOf<Int>(),
                    mods, { add(maxValue + 1) }, chooser)
            }
        }
    }

    @Test fun replace() {
        runToEnd {
            val set = watchableSetOf(1)
            val set2 = watchableSetOf(2)
            set2.bind(set)
            val set3 = set2.readOnly()
            watch(set3) { changes += it}
            assertThat(set.toString(), startsWith("WatchableSet("))
            assertThat(set3.toString(), startsWith("ReadOnlyWatchableSet("))
            changes.expect(SetChange.Initial(setOf(1)))
            set.set(setOf(3))
            changes.expect(SetChange.Remove(1), SetChange.Add(3))
        }
    }
}
