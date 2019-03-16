[io.gladed.watchable](../index.md) / [MutableWatchableBase](index.md) / [subscribe](./subscribe.md)

# subscribe

`open fun subscribe(scope: CoroutineScope, consumer: `[`Subscription`](../-subscription/index.md)`<`[`C`](index.md#C)`>.() -> `[`SubscriptionHandle`](../-subscription-handle/index.md)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Overrides [Watchable.subscribe](../-watchable/subscribe.md)

Initiate and consume a subscription for changes to this [Watchable](../-watchable/index.md), returning a handle for control
over the subscription.

