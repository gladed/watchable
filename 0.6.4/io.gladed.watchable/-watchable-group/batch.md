[io.gladed.watchable](../index.md) / [WatchableGroup](index.md) / [batch](./batch.md)

# batch

`fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, consumer: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GroupChange`](../-group-change/index.md)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Overrides [Watchable.batch](../-watchable/batch.md)

Initiate and consume a subscription for changes to this [Watchable](../-watchable/index.md), returning a handle for control
over the subscription.

