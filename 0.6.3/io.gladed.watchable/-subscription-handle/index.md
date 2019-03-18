[io.gladed.watchable](../index.md) / [SubscriptionHandle](./index.md)

# SubscriptionHandle

`interface SubscriptionHandle`

A handle allowing for management of a subscription to a channel of events.

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel the subscription immediately so that no further events are reported. |
| [close](close.md) | `abstract fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Close the subscription, allowing all outstanding events to be delivered first. |
| [closeAndJoin](close-and-join.md) | `open suspend fun closeAndJoin(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Close the subscription and wait for all events to be processed (shorthand for [close](close.md) then [join](join.md)). |
| [join](join.md) | `abstract suspend fun join(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend until the subscription is closed and all events are drained. |

### Inheritors

| Name | Summary |
|---|---|
| [Subscription](../-subscription/index.md) | `interface Subscription<C> : `[`SubscriptionHandle`](./index.md)<br>A subscription to a channel of events that can be closed or cancelled. |
