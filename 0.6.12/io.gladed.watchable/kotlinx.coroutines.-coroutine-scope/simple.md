[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [simple](./simple.md)

# simple

`fun <S, C : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`S`](simple.md#S)`>> CoroutineScope.simple(watchable: `[`SimpleWatchable`](../-simple-watchable/index.md)`<`[`S`](simple.md#S)`, `[`C`](simple.md#C)`>, func: suspend (`[`S`](simple.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Deliver simplified changes for this [Watchable](../-watchable/index.md) as receiver objects to [func](simple.md#io.gladed.watchable$simple(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.SimpleWatchable((io.gladed.watchable.simple.S, io.gladed.watchable.simple.C)), kotlin.coroutines.SuspendFunction1((io.gladed.watchable.simple.S, kotlin.Unit)))/func) until
the returned [Watcher](../-watcher/index.md) is closed or this [CoroutineScope](#) completes.

