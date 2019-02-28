[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](watch-batches.md#T)`>> CoroutineScope.watchBatches(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](watch-batches.md#T)`, `[`C`](watch-batches.md#C)`>, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](watch-batches.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Receive lists of changes in [block](watch-batches.md#io.gladed.watchable$watchBatches(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watchBatches.T, io.gladed.watchable.watchBatches.C)), kotlin.Function1((kotlin.collections.List((io.gladed.watchable.watchBatches.C)), kotlin.Unit)))/block) for all changes to the [watchable](watch-batches.md#io.gladed.watchable$watchBatches(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watchBatches.T, io.gladed.watchable.watchBatches.C)), kotlin.Function1((kotlin.collections.List((io.gladed.watchable.watchBatches.C)), kotlin.Unit)))/watchable) (starting with its initial state) until the
completion of [watchable](watch-batches.md#io.gladed.watchable$watchBatches(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watchBatches.T, io.gladed.watchable.watchBatches.C)), kotlin.Function1((kotlin.collections.List((io.gladed.watchable.watchBatches.C)), kotlin.Unit)))/watchable)'s context, this [CoroutineScope](#), or the returned [Job](#) is cancelled.

