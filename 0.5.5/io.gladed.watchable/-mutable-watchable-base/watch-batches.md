[io.gladed.watchable](../index.md) / [MutableWatchableBase](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`open fun CoroutineScope.watchBatches(minPeriod: Duration, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Overrides [Watchable.watchBatches](../-watchable/watch-batches.md)

On this [CoroutineScope](#), deliver lists changes on this [Watchable](../-watchable/index.md) to [func](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, java.time.Duration, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial
state. Changes will stop arriving when this scope completes, when the [Watchable](../-watchable/index.md)'s scope completes, when
the returned [Job](#) is cancelled, or if [func](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, java.time.Duration, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) throws.

