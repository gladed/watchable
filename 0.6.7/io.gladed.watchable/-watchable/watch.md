[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watch](./watch.md)

# watch

`open fun watch(scope: CoroutineScope, func: suspend (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)

Deliver all changes from this [Watchable](index.md) to [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects.

