[io.gladed.watchable](../index.md) / [Watchable](./index.md)

# Watchable

`interface Watchable<out C : `[`Change`](../-change/index.md)`>`

An object that allows you to watch for changes of type [C](index.md#C).

Each watch operation takes a [CoroutineScope](#). Callbacks are delivered using this scope's context, and stop
automatically when this scope cancels or completes.

Each watch operation also returns a [Watcher](../-watcher/index.md) which may be used to independently cancel or join the watch
operation.

### Functions

| Name | Summary |
|---|---|
| [batch](batch.md) | `abstract fun batch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](./index.md) to [func](batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects until [scope](batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [waitFor](wait-for.md) | `open suspend fun waitFor(scope: CoroutineScope, func: () -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend, calling [func](wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) as changes arrive, and return when [func](wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) returns true. |
| [watch](watch.md) | `open fun watch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](./index.md) to [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects until [scope](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [MutableWatchable](../-mutable-watchable/index.md) | `interface MutableWatchable<M, C : `[`Change`](../-change/index.md)`> : `[`Watchable`](./index.md)`<`[`C`](../-mutable-watchable/index.md#C)`>`<br>A [Watchable](./index.md) containing a mutable object of type [M](../-mutable-watchable/index.md#M), which can both generate and accept changes of type [C](../-mutable-watchable/index.md#C). |
| [ReadOnlyWatchableValue](../-read-only-watchable-value/index.md) | `interface ReadOnlyWatchableValue<T> : `[`Watchable`](./index.md)`<`[`ValueChange`](../-value-change/index.md)`<`[`T`](../-read-only-watchable-value/index.md#T)`>>`<br>A [Watchable](./index.md) value of type [T](../-read-only-watchable-value/index.md#T) which may not be replaced by the reference holder. |
| [SimpleWatchable](../-simple-watchable/index.md) | `interface SimpleWatchable<S, C : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`S`](../-simple-watchable/index.md#S)`>> : `[`Watchable`](./index.md)`<`[`C`](../-simple-watchable/index.md#C)`>`<br>A [Watchable](./index.md) that allows for a more verbose series of simpler changes. |
| [WatchableBase](../-watchable-base/index.md) | `abstract class WatchableBase<C : `[`Change`](../-change/index.md)`> : `[`Watchable`](./index.md)`<`[`C`](../-watchable-base/index.md#C)`>`<br>Base for an object that generates change events of type [C](../-watchable-base/index.md#C) as its underlying data changes. |
| [WatchableGroup](../-watchable-group/index.md) | `class WatchableGroup : `[`Watchable`](./index.md)`<`[`GroupChange`](../-group-change/index.md)`>`<br>A group of [Watchable](./index.md) objects that can be watched for any change, which arrives as a [GroupChange](../-group-change/index.md). Can also be created with [group](../group.md). |
