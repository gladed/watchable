[io.gladed.watchable.store](index.md) / [toWatchableMap](./to-watchable-map.md)

# toWatchableMap

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](to-watchable-map.md#T)`>.toWatchableMap(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`WatchableMap`](../io.gladed.watchable/-watchable-map/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`T`](to-watchable-map.md#T)`>`

Return a [WatchableMap](../io.gladed.watchable/-watchable-map/index.md) containing all items in this [Store](-store/index.md), and persisting changes from the map to the store
as they happen until [scope](to-watchable-map.md#io.gladed.watchable.store$toWatchableMap(io.gladed.watchable.store.Store((io.gladed.watchable.store.toWatchableMap.T)), kotlinx.coroutines.CoroutineScope, kotlin.Long)/scope) completes. If items implement [Container](-container/index.md) their contents also trigger
a put into the background store.

Note: initial population of the map will occur concurrently on [scope](to-watchable-map.md#io.gladed.watchable.store$toWatchableMap(io.gladed.watchable.store.Store((io.gladed.watchable.store.toWatchableMap.T)), kotlinx.coroutines.CoroutineScope, kotlin.Long)/scope).

