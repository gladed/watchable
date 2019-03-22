[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watchSimple](./watch-simple.md)

# watchSimple

`fun <T, V, C : `[`Change`](../-change/index.md)`<`[`T`](watch-simple.md#T)`, `[`V`](watch-simple.md#V)`>> CoroutineScope.watchSimple(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](watch-simple.md#T)`, `[`V`](watch-simple.md#V)`, `[`C`](watch-simple.md#C)`>, func: suspend `[`SimpleChange`](../-simple-change/index.md)`<`[`V`](watch-simple.md#V)`>.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)

Deliver changes for this [Watchable](../-watchable/index.md) to [func](watch-simple.md#io.gladed.watchable$watchSimple(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watchSimple.T, io.gladed.watchable.watchSimple.V, io.gladed.watchable.watchSimple.C)), kotlin.SuspendFunction1((io.gladed.watchable.SimpleChange((io.gladed.watchable.watchSimple.V)), kotlin.Unit)))/func), starting with its initial state, until
the returned [WatchHandle](../-watch-handle/index.md) is closed or this [CoroutineScope](#) completes.

