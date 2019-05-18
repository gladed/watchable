[io.gladed.watchable.store](index.md) / [bind](./bind.md)

# bind

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](bind.md#T)`>.bind(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, map: `[`WatchableMap`](../io.gladed.watchable/-watchable-map/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`T`](bind.md#T)`>): `[`Watcher`](../io.gladed.watchable/-watcher/index.md)

Load up a [WatchableMap](../io.gladed.watchable/-watchable-map/index.md) with all items in this [Store](-store/index.md), and persisting changes from the map to the store
as they happen until [scope](bind.md#io.gladed.watchable.store$bind(io.gladed.watchable.store.Store((io.gladed.watchable.store.bind.T)), kotlinx.coroutines.CoroutineScope, kotlin.Long, io.gladed.watchable.WatchableMap((kotlin.String, io.gladed.watchable.store.bind.T)))/scope) completes.

Changes to items implementing [Container](-container/index.md) will trigger a put into the store.

This is a one-way bind; external changes to the [Store](-store/index.md) will not be reflected in [map](bind.md#io.gladed.watchable.store$bind(io.gladed.watchable.store.Store((io.gladed.watchable.store.bind.T)), kotlinx.coroutines.CoroutineScope, kotlin.Long, io.gladed.watchable.WatchableMap((kotlin.String, io.gladed.watchable.store.bind.T)))/map).

