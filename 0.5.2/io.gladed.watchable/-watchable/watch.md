[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watch](./watch.md)

# watch

`open fun CoroutineScope.watch(block: (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Receive individual changes in [block](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/block) for all changes to the [watchable](#) (starting with its initial state)
until the completion of this [Watchable](index.md)'s context, this [CoroutineScope](#), or the returned [Job](#) is cancelled.

