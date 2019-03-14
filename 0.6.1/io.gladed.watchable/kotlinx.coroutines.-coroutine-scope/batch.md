[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [batch](./batch.md)

# batch

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](batch.md#T)`>> CoroutineScope.batch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](batch.md#T)`, `[`C`](batch.md#C)`>, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](batch.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Deliver multiple changes for this [Watchable](../-watchable/index.md) to [func](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.batch.T, io.gladed.watchable.batch.C)), kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.batch.C)), kotlin.Unit)))/func), starting with its initial state, until
the returned job is cancelled or this [CoroutineScope](#) completes.

`fun <U> CoroutineScope.batch(input: ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`U`](batch.md#U)`>>, periodMillis: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`U`](batch.md#U)`>>`

For a given receive channel of lists of items, emit combined lists of items no more frequently than every
[periodMillis](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, kotlinx.coroutines.channels.ReceiveChannel((kotlin.collections.List((io.gladed.watchable.batch.U)))), kotlin.Long)/periodMillis), starting now. If [periodMillis](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, kotlinx.coroutines.channels.ReceiveChannel((kotlin.collections.List((io.gladed.watchable.batch.U)))), kotlin.Long)/periodMillis) is non-positive, returns [input](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, kotlinx.coroutines.channels.ReceiveChannel((kotlin.collections.List((io.gladed.watchable.batch.U)))), kotlin.Long)/input) as-is (unbatched).

