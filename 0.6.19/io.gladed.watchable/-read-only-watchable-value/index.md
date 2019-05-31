[io.gladed.watchable](../index.md) / [ReadOnlyWatchableValue](./index.md)

# ReadOnlyWatchableValue

`interface ReadOnlyWatchableValue<T> : `[`Watchable`](../-watchable/index.md)`<`[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>`

A [Watchable](../-watchable/index.md) value of type [T](index.md#T) which may not be replaced by the reference holder.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T)<br>Direct access to the current value inside the container. |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](../-watchable/batch.md) | `abstract fun batch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects until [scope](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [waitFor](../-watchable/wait-for.md) | `open suspend fun waitFor(scope: CoroutineScope, func: () -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend, calling [func](../-watchable/wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) as changes arrive, and return when [func](../-watchable/wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) returns true. |
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects until [scope](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableValue](../-watchable-value/index.md) | `interface WatchableValue<T> : `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`MutableValue`](../-mutable-value/index.md)`<`[`T`](../-watchable-value/index.md#T)`>, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-watchable-value/index.md#T)`>>, `[`ReadOnlyWatchableValue`](./index.md)`<`[`T`](../-watchable-value/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) value of [T](../-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create. |
