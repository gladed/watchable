[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watch](./watch.md)

# watch

`open fun watch(scope: CoroutineScope, func: suspend (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Deliver changes for this [Watchable](index.md) to [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until
the returned [Job](#) is cancelled or the [scope](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes.

