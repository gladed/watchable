[io.gladed.watchable](../index.md) / [SimpleWatchable](./index.md)

# SimpleWatchable

`interface SimpleWatchable<S, C : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`S`](index.md#S)`>> : `[`Watchable`](../-watchable/index.md)`<`[`C`](index.md#C)`>`

A [Watchable](../-watchable/index.md) that allows for a more verbose series of simpler changes.

### Functions

| Name | Summary |
|---|---|
| [simple](simple.md) | `open fun simple(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`S`](index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver simplified changes to [func](simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/func) until [scope](simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/scope) completes. |

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
| [ReadOnlyWatchableList](../-read-only-watchable-list.md) | `interface ReadOnlyWatchableList<T> : `[`SimpleWatchable`](./index.md)`<`[`ListChange.Simple`](../-list-change/-simple/index.md)`<`[`T`](../-read-only-watchable-list.md#T)`>, `[`ListChange`](../-list-change/index.md)`<`[`T`](../-read-only-watchable-list.md#T)`>>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-read-only-watchable-list.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableMap](../-read-only-watchable-map.md) | `interface ReadOnlyWatchableMap<K, V> : `[`SimpleWatchable`](./index.md)`<`[`MapChange.Simple`](../-map-change/-simple/index.md)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>, `[`MapChange`](../-map-change/index.md)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>>, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableSet](../-read-only-watchable-set.md) | `interface ReadOnlyWatchableSet<T> : `[`SimpleWatchable`](./index.md)`<`[`SetChange.Simple`](../-set-change/-simple/index.md)`<`[`T`](../-read-only-watchable-set.md#T)`>, `[`SetChange`](../-set-change/index.md)`<`[`T`](../-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-read-only-watchable-set.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder. |
