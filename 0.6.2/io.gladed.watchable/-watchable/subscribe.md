[io.gladed.watchable](../index.md) / [Watchable](index.md) / [subscribe](./subscribe.md)

# subscribe

`abstract fun subscribe(scope: CoroutineScope, consumer: `[`Subscription`](../-subscription/index.md)`<`[`C`](index.md#C)`>.() -> `[`SubscriptionHandle`](../-subscription-handle/index.md)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Initiate and consume a subscription for changes to this [Watchable](index.md), returning a handle for control
over the subscription.

