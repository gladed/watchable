[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watch](./watch.md)

# watch

`fun <C : `[`Change`](../-change/index.md)`> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`C`](watch.md#C)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`C`](watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Deliver changes for this [Watchable](../-watchable/index.md) to [func](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.C)), kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.watch.C, kotlin.Unit)))/func) until the returned [Watcher](../-watcher/index.md) is closed or this
[CoroutineScope](#) completes.

