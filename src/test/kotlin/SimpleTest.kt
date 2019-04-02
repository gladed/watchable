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

import io.gladed.watchable.SimpleChange
import io.gladed.watchable.group
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SimpleTest {
    @Test fun `watch value simply`() = runBlocking {
        // TODO: Replace these when simple syntax is back on line
//        val value = watchableValueOf(1)
//        val channel = Channel<SimpleChange<Int>>(Channel.UNLIMITED)
//        value.watchSimple(this) { channel.send(this) }
//        channel.expect(SimpleChange(add = 1))
//        value.set(2)
//        channel.expect(SimpleChange(add = 2, remove = 1))
    }

//    @Test fun `watch list simply`() = runBlocking {
//        val list = watchableListOf(1, 2)
//        val channel = Channel<SimpleChange<Int>>(Channel.UNLIMITED)
//        list.watchSimple(this) { channel.send(this) }
//        channel.expect(SimpleChange(add = 1), SimpleChange(add = 2))
//        list.use { removeAt(1); set(0, 3); add(4) }
//        channel.expect(
//            SimpleChange(remove = 2),
//            SimpleChange(remove = 1, add = 3),
//            SimpleChange(add = 4))
//    }
//
//    @Test fun `watch map simply`() = runBlocking {
//        val map = watchableMapOf(1 to "1")
//        val channel = Channel<SimpleChange<String>>(Channel.UNLIMITED)
//        watchSimple(map) { channel.send(this) }
//        channel.expect(SimpleChange(add = "1"))
//        map.use { put(2, "2"); remove(1); put(2, "22") }
//        channel.expect(
//            SimpleChange(add = "2"),
//            SimpleChange(remove = "1"),
//            SimpleChange(remove = "2", add = "22"))
//    }
//
//    @Test fun `watch set simply`() = runBlocking {
//        val set = watchableSetOf(1, 2)
//        val channel = Channel<SimpleChange<Int>>(Channel.UNLIMITED)
//        set.watchSimple(this) { channel.send(this) }
//        channel.expect(SimpleChange(add = 1), SimpleChange(add = 2))
//        set.use { remove(1); add(3) }
//        channel.expect(
//            SimpleChange(remove = 1),
//            SimpleChange(add = 3))
//    }
//
//    @Test fun `watch group simply`() = runBlocking {
//        val list = watchableListOf(1, 2)
//        val set = watchableSetOf("1", "2")
//        val channel = Channel<SimpleChange<Any>>(Channel.UNLIMITED)
//        group(list, set).watchSimple(this) { channel.send(this) }
//        channel.expect(SimpleChange(add = list), SimpleChange(add = set))
//        list.use { removeAt(1) }
//        set.use { add("3") }
//        channel.expect(
//            SimpleChange(add = list),
//            SimpleChange(add = set))
//    }
}
