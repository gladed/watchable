[io.gladed.watchable](../index.md) / [ReadOnlyWatchableValue](./index.md)

# ReadOnlyWatchableValue

`interface ReadOnlyWatchableValue<T> : `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>`

A [Watchable](../-watchable/index.md) value of type [T](index.md#T) which may not be replaced by the reference holder.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T)<br>The current value. |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](../-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver lists of changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the returned job is cancelled or the [scope](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [get](../-watchable/get.md) | `abstract suspend fun get(): `[`T`](../-watchable/index.md#T)<br>Return the current value of [T](../-watchable/index.md#T). |
| [subscribe](../-watchable/subscribe.md) | `abstract fun subscribe(scope: CoroutineScope): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>>`<br>Return a channel which will receive successive lists of changes as they occur. |
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the returned job is cancelled or the [scope](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableValue](../-watchable-value/index.md) | `class WatchableValue<T> : `[`MutableWatchableBase`](../-mutable-watchable-base/index.md)`<`[`T`](../-watchable-value/index.md#T)`, `[`T`](../-watchable-value/index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-watchable-value/index.md#T)`>>, `[`ReadOnlyWatchableValue`](./index.md)`<`[`T`](../-watchable-value/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) value of [T](../-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create. |
