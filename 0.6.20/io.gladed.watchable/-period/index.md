[io.gladed.watchable](../index.md) / [Period](./index.md)

# Period

`object Period`

Defines special values for watcher timing.

When period is &gt;0, changes are collected and delivered no more frequently than that many milliseconds.

### Properties

| Name | Summary |
|---|---|
| [IMMEDIATE](-i-m-m-e-d-i-a-t-e.md) | `const val IMMEDIATE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>A watcher with this period run very soon after the change is made. This is the default for all watching operations. |
| [INLINE](-i-n-l-i-n-e.md) | `const val INLINE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>A watcher that runs before the change is fully applied. If it throws the change will be rolled back and the exception re-thrown at the site of the change. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
