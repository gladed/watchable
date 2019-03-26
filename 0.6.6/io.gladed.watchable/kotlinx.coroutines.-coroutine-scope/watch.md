[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watch](./watch.md)

# watch

`fun <T, V, C : `[`Change`](../-change/index.md)`<`[`T`](watch.md#T)`, `[`V`](watch.md#V)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](watch.md#T)`, `[`V`](watch.md#V)`, `[`C`](watch.md#C)`>, func: suspend (`[`C`](watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)

Deliver changes for this [Watchable](../-watchable/index.md) to [func](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.V, io.gladed.watchable.watch.C)), kotlin.SuspendFunction1((io.gladed.watchable.watch.C, kotlin.Unit)))/func), starting with its initial state, until
the returned [WatchHandle](../-watch-handle/index.md) is closed or this [CoroutineScope](#) completes.

