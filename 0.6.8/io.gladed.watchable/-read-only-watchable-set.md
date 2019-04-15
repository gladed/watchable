[io.gladed.watchable](index.md) / [ReadOnlyWatchableSet](./-read-only-watchable-set.md)

# ReadOnlyWatchableSet

`interface ReadOnlyWatchableSet<T> : `[`SimpleWatchable`](-simple-watchable/index.md)`<`[`SetChange.Simple`](-set-change/-simple/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-read-only-watchable-set.md#T)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder.

### Inherited Functions

| Name | Summary |
|---|---|
| [simple](-simple-watchable/simple.md) | `open suspend fun simple(scope: CoroutineScope, func: suspend (`[`S`](-simple-watchable/index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](-watcher/index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableList](kotlin.collections.-collection/to-watchable-list.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](-watchable-list/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableSet](kotlin.collections.-collection/to-watchable-set.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](-watchable-set/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](-watchable-set/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableValue](to-watchable-value.md) | `fun <T> `[`T`](to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](-watchable-value/index.md)`<`[`T`](to-watchable-value.md#T)`>`<br>Convert this [T](to-watchable-value.md#T) to a watchable value of [T](to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableSet](-watchable-set/index.md) | `class WatchableSet<T> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`T`](-watchable-set/index.md#T)`, `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](./-read-only-watchable-set.md)`<`[`T`](-watchable-set/index.md#T)`>`<br>A [Watchable](-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](watchable-set-of.md) to create. |
