[io.gladed.watchable](index.md) / [ReadOnlyWatchableSet](./-read-only-watchable-set.md)

# ReadOnlyWatchableSet

`interface ReadOnlyWatchableSet<T> : `[`SimpleWatchable`](-simple-watchable/index.md)`<`[`SetChange.Simple`](-set-change/-simple/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-read-only-watchable-set.md#T)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder.

### Inherited Functions

| Name | Summary |
|---|---|
| [simple](-simple-watchable/simple.md) | `open fun simple(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`S`](-simple-watchable/index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](-watcher/index.md)<br>Deliver simplified changes to [func](-simple-watchable/simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/func) until [scope](-simple-watchable/simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableList](kotlin.collections.-iterable/to-watchable-list.md) | `fun <T> `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](kotlin.collections.-iterable/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](-watchable-list/index.md)`<`[`T`](kotlin.collections.-iterable/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](-watchable-list/index.md) containing the elements of this [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html). |
| [toWatchableSet](kotlin.collections.-iterable/to-watchable-set.md) | `fun <T> `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](kotlin.collections.-iterable/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](-watchable-set/index.md)`<`[`T`](kotlin.collections.-iterable/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](-watchable-set/index.md) containing the elements of this [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html). |
| [toWatchableValue](to-watchable-value.md) | `fun <T> `[`T`](to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](-watchable-value/index.md)`<`[`T`](to-watchable-value.md#T)`>`<br>Convert this [T](to-watchable-value.md#T) to a watchable value of [T](to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableSet](-watchable-set/index.md) | `interface WatchableSet<T> : `[`MutableWatchable`](-mutable-watchable/index.md)`<`[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](./-read-only-watchable-set.md)`<`[`T`](-watchable-set/index.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](watchable-set-of.md) to create. |
