[io.gladed.watchable](../index.md) / [WatchableMap](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`MapChange`](../-map-change/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Overrides [Watchable.watchBatches](../-watchable/watch-batches.md)

Receive lists of changes in [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) for all changes to the [watchable](#) (starting with its initial state) until
the completion of this [Watchable](../-watchable/index.md)'s context, this [CoroutineScope](#), or the returned [Job](#) is cancelled.

