[io.gladed.watchable](index.md) / [ReadOnlyWatchableList](./-read-only-watchable-list.md)

# ReadOnlyWatchableList

`interface ReadOnlyWatchableList<T> : `[`SimpleWatchable`](-simple-watchable/index.md)`<`[`ListChange.Simple`](-list-change/-simple/index.md)`<`[`T`](-read-only-watchable-list.md#T)`>, `[`ListChange`](-list-change/index.md)`<`[`T`](-read-only-watchable-list.md#T)`>>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-read-only-watchable-list.md#T)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder.

### Inherited Functions

| Name | Summary |
|---|---|
| [simple](-simple-watchable/simple.md) | `open fun simple(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`S`](-simple-watchable/index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](-watcher/index.md)<br>Deliver simplified changes to [func](-simple-watchable/simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/func) until [scope](-simple-watchable/simple.md#io.gladed.watchable.SimpleWatchable$simple(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.SimpleWatchable.S, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableList](kotlin.collections.-iterable/to-watchable-list.md) | `fun <T> `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](kotlin.collections.-iterable/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](-watchable-list/index.md)`<`[`T`](kotlin.collections.-iterable/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](-watchable-list/index.md) containing the elements of this [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html). |
| [toWatchableSet](kotlin.collections.-iterable/to-watchable-set.md) | `fun <T> `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](kotlin.collections.-iterable/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](-watchable-set/index.md)`<`[`T`](kotlin.collections.-iterable/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](-watchable-set/index.md) containing the elements of this [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html). |
| [toWatchableValue](to-watchable-value.md) | `fun <T> `[`T`](to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](-watchable-value/index.md)`<`[`T`](to-watchable-value.md#T)`>`<br>Convert this [T](to-watchable-value.md#T) to a watchable value of [T](to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableList](-watchable-list/index.md) | `interface WatchableList<T> : `[`MutableWatchable`](-mutable-watchable/index.md)`<`[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`T`](-watchable-list/index.md#T)`>, `[`ListChange`](-list-change/index.md)`<`[`T`](-watchable-list/index.md#T)`>>, `[`ReadOnlyWatchableList`](./-read-only-watchable-list.md)`<`[`T`](-watchable-list/index.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](watchable-list-of.md) to create. |
