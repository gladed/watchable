[io.gladed.watchable](../index.md) / [Watchable](index.md) / [batch](./batch.md)

# batch

`abstract fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, consumer: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Initiate and consume a subscription for changes to this [Watchable](index.md), returning a handle for control
over the subscription.

