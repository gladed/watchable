[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`abstract fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Receive lists of changes in [block](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) for all changes to the [watchable](#) (starting with its initial state) until
the completion of this [Watchable](index.md)'s context, this [CoroutineScope](#), or the returned [Job](#) is cancelled.

