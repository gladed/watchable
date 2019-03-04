[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`abstract fun watchBatches(scope: CoroutineScope, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Receive lists of changes in [func](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) for all changes to this [Watchable](index.md) (starting with its initial state)
until this [Watchable](index.md)'s scope completes OR [scope](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes OR the returned [Job](#) is cancelled.

