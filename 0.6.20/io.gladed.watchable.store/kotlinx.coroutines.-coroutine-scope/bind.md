[io.gladed.watchable.store](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [bind](./bind.md)

# bind

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.bind(store: `[`Store`](../-store/index.md)`<`[`T`](bind.md#T)`>, map: `[`WatchableMap`](../../io.gladed.watchable/-watchable-map/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`T`](bind.md#T)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Watcher`](../../io.gladed.watchable/-watcher/index.md)

Load up a [WatchableMap](../../io.gladed.watchable/-watchable-map/index.md) with all items in this [Store](../-store/index.md), and persisting changes from the map to the store
as they happen until [scope](#) completes.

Changes to items implementing [Container](../-container/index.md) will trigger a put into the store.

This is a one-way bind; external changes to the [Store](../-store/index.md) will not be reflected in [map](bind.md#io.gladed.watchable.store$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.store.Store((io.gladed.watchable.store.bind.T)), io.gladed.watchable.WatchableMap((kotlin.String, io.gladed.watchable.store.bind.T)), kotlin.Long)/map).

