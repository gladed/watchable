[io.gladed.watchable](../index.md) / [Subscription](index.md) / [batch](./batch.md)

# batch

`abstract fun batch(scope: CoroutineScope, periodMillis: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, block: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Consume batches of values from this subscription.

