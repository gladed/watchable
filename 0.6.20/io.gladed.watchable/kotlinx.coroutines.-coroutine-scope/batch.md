[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [batch](./batch.md)

# batch

`fun <C : `[`Change`](../-change/index.md)`> CoroutineScope.batch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`C`](batch.md#C)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](batch.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Deliver multiple changes for this [Watchable](../-watchable/index.md) to [func](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.batch.C)), kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.batch.C)), kotlin.Unit)))/func) until the returned [Watcher](../-watcher/index.md) is closed or this
[CoroutineScope](#) completes.

