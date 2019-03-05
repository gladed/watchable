[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`abstract fun CoroutineScope.watchBatches(func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

On this [CoroutineScope](#), deliver lists changes on this [Watchable](index.md) to [func](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial
state. Changes will stop arriving when this scope completes, when the [Watchable](index.md)'s scope completes, when
the returned [Job](#) is cancelled, or if [func](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) throws.

