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

import io.gladed.watchable.Change
import io.gladed.watchable.MutableWatchable
import io.gladed.watchable.WatchableMap
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import io.gladed.watchable.watchableMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

class WatchableMapTest : CoroutineScope {

    @Rule @JvmField val scope = ScopeRule(Dispatchers.Default)
    override val coroutineContext = scope.coroutineContext

    private val chooser = Chooser(0) // Stable seed makes tests repeatable
    private val maxKey = 500

    private val modifications = listOf<MutableMap<Int, String>.() -> Unit>(
        // Remove
        { chooser(keys)?.also { remove(it) } },

        // Replace (by map)
        { chooser(keys)?.also { key -> replace(key, this[key]!! + key) } },

        // Add/replace
        { chooser(maxKey).also { key -> this[key] = key.toString() } },

        // Add or replace (again, so we get a lot of insertions)
        { chooser(maxKey).also { key -> this[key] = key.toString() } },

        // Replace (by entry)
        { chooser(entries)?.apply { setValue("$value$key") } }
    )

//    TODO: Confirm a few things about the map
//    map.use {
//        println("Map first entry is ${entries.first()} and its hashcode is ${entries.first().hashCode()}")
//        // Show that we can't add entries this way, only through "put"
//        try {
//            entries.add(entries.first())
//        } catch (e: UnsupportedOperationException) {
//            // Expected
//        }
//    }

    @Test fun changes() {
        val count = 1000
        val elapsed = measureTimeMillis {
            runToEnd {
                iterateMutable(
                    this,
                    watchableMapOf(),
                    watchableMapOf(),
                    modifications,
                    { this[maxKey + 1] = "end" },
                    chooser
                )
            }
        }
        // With sync: 31 micros for 100k iters
        log("$count in $elapsed ms. ${elapsed * 1000 / count } μs per iteration.")
    }
}

suspend fun <M : T, T, C : Change<T>> iterateMutable(
    scope: CoroutineScope,
    one: MutableWatchable<M, T, C>,
    two: MutableWatchable<M, T, C>,
    mods: List<M.() -> Unit>,
    closer: M.() -> Unit,
    chooser: Chooser,
    count: Int = 1000) {

    scope.bind(one, two)
    scope.watch(two) {
        scope.launch {
            if (0 == chooser(10)) two.get()
        }
    }

    (0 until count).map {
        scope.launch {
            one.use {
                chooser(mods)!!(this)
            }
        }
    }.joinAll()

    println("----sending closer----")
    one.use { closer() }

    scope.watch(two) {
        scope.launch {
            if (one.get() == two.get()) {
                println("DONE, cancelling")
                scope.coroutineContext.cancel()
                yield()
            }
        }
    }
    delay(3000)
    assertEquals(one.get(), two.get())
}
