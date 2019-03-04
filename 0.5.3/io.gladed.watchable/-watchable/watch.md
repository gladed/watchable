[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watch](./watch.md)

# watch

`open fun watch(scope: CoroutineScope, func: (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Receive individual changes in [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) for all changes to this active [Watchable](index.md) (starting with its initial state)
until this [Watchable](index.md)'s scope completes OR [scope](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes OR the returned [Job](#) is cancelled.

