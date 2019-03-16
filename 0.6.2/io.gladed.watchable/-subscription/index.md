[io.gladed.watchable](../index.md) / [Subscription](./index.md)

# Subscription

`interface Subscription<C> : `[`SubscriptionHandle`](../-subscription-handle/index.md)`, ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>>`

A subscription to a channel of events that can be closed or cancelled.

### Functions

| Name | Summary |
|---|---|
| [batch](batch.md) | `abstract fun batch(scope: CoroutineScope, periodMillis: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, block: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)<br>Consume batches of values from this subscription. |

### Inherited Functions

| Name | Summary |
|---|---|
| [cancel](../-subscription-handle/cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel the subscription immediately so that no further events are reported. |
| [close](../-subscription-handle/close.md) | `abstract fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Close the subscription, allowing all outstanding events to be delivered first. |
| [closeAndJoin](../-subscription-handle/close-and-join.md) | `open suspend fun closeAndJoin(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Flush all events and close the channel (shorthand for [close](../-subscription-handle/close.md) then [join](../-subscription-handle/join.md)). |
| [join](../-subscription-handle/join.md) | `abstract suspend fun join(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend until the subscription is complete. |
