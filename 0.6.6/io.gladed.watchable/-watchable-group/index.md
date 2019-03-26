[io.gladed.watchable](../index.md) / [WatchableGroup](./index.md)

# WatchableGroup

`class WatchableGroup<out T, out V, C : `[`Change`](../-change/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`>> : `[`Watchable`](../-watchable/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>, `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>, `[`GroupChange`](../-group-change/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>`

A group of [Watchable](../-watchable/index.md) objects that can be watched for any change, which arrives as a [GroupChange](../-group-change/index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WatchableGroup(watchables: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>)`<br>A group of [Watchable](../-watchable/index.md) objects that can be watched for any change, which arrives as a [GroupChange](../-group-change/index.md). |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>`<br>Return an immutable copy of the current value of [T](../-watchable/index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [batch](batch.md) | `fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GroupChange`](../-group-change/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects. |

### Inherited Functions

| Name | Summary |
|---|---|
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects. |
| [watchSimple](../-watchable/watch-simple.md) | `open fun watchSimple(scope: CoroutineScope, func: suspend `[`SimpleChange`](../-simple-change/index.md)`<`[`V`](../-watchable/index.md#V)`>.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/watch-simple.md#io.gladed.watchable.Watchable$watchSimple(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.SimpleChange((io.gladed.watchable.Watchable.V)), kotlin.Unit)))/func) receiving [SimpleChange](../-simple-change/index.md) objects. |
