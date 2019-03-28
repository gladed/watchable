[io.gladed.watchable](index.md) / [ReadOnlyWatchableSet](./-read-only-watchable-set.md)

# ReadOnlyWatchableSet

`interface ReadOnlyWatchableSet<T> : `[`Watchable`](-watchable/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-read-only-watchable-set.md#T)`>, `[`T`](-read-only-watchable-set.md#T)`, `[`SetChange`](-set-change/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-read-only-watchable-set.md#T)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder.

### Inherited Properties

| Name | Summary |
|---|---|
| [value](-watchable/value.md) | `abstract val value: `[`T`](-watchable/index.md#T)<br>Return an immutable copy of the current value of [T](-watchable/index.md#T). |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](-watchable/batch.md) | `abstract fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](-watch-handle/index.md)<br>Deliver all changes from this [Watchable](-watchable/index.md) to [func](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](-change/index.md) objects. |
| [watch](-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](-watch-handle/index.md)<br>Deliver all changes from this [Watchable](-watchable/index.md) to [func](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](-change/index.md) objects. |
| [watchSimple](-watchable/watch-simple.md) | `open fun watchSimple(scope: CoroutineScope, func: suspend `[`SimpleChange`](-simple-change/index.md)`<`[`V`](-watchable/index.md#V)`>.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](-watch-handle/index.md)<br>Deliver all changes from this [Watchable](-watchable/index.md) to [func](-watchable/watch-simple.md#io.gladed.watchable.Watchable$watchSimple(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.SimpleChange((io.gladed.watchable.Watchable.V)), kotlin.Unit)))/func) receiving [SimpleChange](-simple-change/index.md) objects. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableList](kotlin.collections.-collection/to-watchable-list.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](-watchable-list/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableSet](kotlin.collections.-collection/to-watchable-set.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](-watchable-set/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](-watchable-set/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableSet](-watchable-set/index.md) | `class WatchableSet<T> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`T`](-watchable-set/index.md#T)`, `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](./-read-only-watchable-set.md)`<`[`T`](-watchable-set/index.md#T)`>`<br>A [Watchable](-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](watchable-set-of.md) to create. |
