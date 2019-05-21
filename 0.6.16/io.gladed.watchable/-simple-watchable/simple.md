[io.gladed.watchable](../index.md) / [SimpleWatchable](index.md) / [simple](./simple.md)

# simple

`open fun simple(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`S`](index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Deliver simplified changes to [func](simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/func) until [scope](simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/scope) completes.

