[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watchBatches](./watch-batches.md)

# watchBatches

`abstract fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Deliver changes to [block](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) using this [CoroutineScope](#) until it terminates or until the returned [Job](#) is
cancelled. Each change is processed to completion (e.g. [block](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) must return) before the next change is
delivered.

Calling [watch](watch.md) will immediately launch a call to [block](watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) to inform it of the [Watchable](index.md)'s initial value.
Further calls will arrive if and when this [Watchable](index.md) changes.
