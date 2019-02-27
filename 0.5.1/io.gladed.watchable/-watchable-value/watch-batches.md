[io.gladed.watchable](../index.md) / [WatchableValue](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Overrides [Watchable.watchBatches](../-watchable/watch-batches.md)

Deliver changes to [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) using this [CoroutineScope](#) until it terminates or until the returned [Job](#) is
cancelled. Each change is processed to completion (e.g. [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) must return) before the next change is
delivered.

Calling [watch](../-watchable/watch.md) will immediately launch a call to [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) to inform it of the [Watchable](../-watchable/index.md)'s initial value.
Further calls will arrive if and when this [Watchable](../-watchable/index.md) changes.

