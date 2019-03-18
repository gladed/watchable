[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [batch](./batch.md)

# batch

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](batch.md#T)`>> CoroutineScope.batch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](batch.md#T)`, `[`C`](batch.md#C)`>, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](batch.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Deliver multiple changes for this [Watchable](../-watchable/index.md) to [func](batch.md#io.gladed.watchable$batch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.batch.T, io.gladed.watchable.batch.C)), kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.batch.C)), kotlin.Unit)))/func), starting with its initial state, until
the returned [SubscriptionHandle](../-subscription-handle/index.md) is closed or this [CoroutineScope](#) completes.

