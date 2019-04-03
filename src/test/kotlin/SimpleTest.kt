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
import io.gladed.watchable.MapChange
import io.gladed.watchable.SetChange
import io.gladed.watchable.simple
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableMapOf
import io.gladed.watchable.watchableSetOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SimpleTest {
    @Test fun `watch list simply`() = runBlocking {
        val list = watchableListOf(1, 2)
        val channel = Channel<ListChange.Simple<Int>>(Channel.UNLIMITED)
        simple(list) { channel.send(this) }

        channel.expect(
            ListChange.Simple(index = 0, item = 1),
            ListChange.Simple(index = 1, item = 2))

        list.use { removeAt(1); set(0, 3); add(4) }
        channel.expect(
            ListChange.Simple(index = 1),
            ListChange.Simple(index = 0, item = 3, insert = false),
            ListChange.Simple(index = 1, item = 4))
    }

    @Test fun `watch map simply`() = runBlocking {
        val map = watchableMapOf(1 to "1")
        val channel = Channel<MapChange.Simple<Int, String>>(Channel.UNLIMITED)
        simple(map) { channel.send(this) }
        channel.expect(MapChange.Simple(key = 1, value = "1"))
        map.use { put(2, "2"); remove(1); put(2, "22") }
        channel.expect(
            MapChange.Simple(key = 2, value = "2"),
            MapChange.Simple(key = 1),
            MapChange.Simple(key = 2, value = "22"))
    }
//
    @Test fun `watch set simply`() = runBlocking {
        val set = watchableSetOf(1, 2)
        val channel = Channel<SetChange.Simple<Int>>(Channel.UNLIMITED)
        simple(set) { channel.send(this) }
        channel.expect(SetChange.Simple(add = 1), SetChange.Simple(add = 2))
        set.use { remove(1); add(3) }
        channel.expect(
            SetChange.Simple(remove = 1),
            SetChange.Simple(add = 3))
    }
}
