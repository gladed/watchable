[io.gladed.watchable](../index.md) / [WatchableGroup](index.md) / [subscribe](./subscribe.md)

# subscribe

`fun subscribe(scope: CoroutineScope, consumer: `[`Subscription`](../-subscription/index.md)`<`[`GroupChange`](../-group-change/index.md)`>.() -> `[`SubscriptionHandle`](../-subscription-handle/index.md)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)

Overrides [Watchable.subscribe](../-watchable/subscribe.md)

Initiate and consume a subscription for changes to this [Watchable](../-watchable/index.md), returning a handle for control
over the subscription.

