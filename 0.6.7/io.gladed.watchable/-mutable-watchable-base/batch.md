[io.gladed.watchable](../index.md) / [MutableWatchableBase](index.md) / [batch](./batch.md)

# batch

`open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)

Overrides [Watchable.batch](../-watchable/batch.md)

Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects.

