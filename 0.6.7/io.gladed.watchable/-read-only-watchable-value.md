[io.gladed.watchable](index.md) / [ReadOnlyWatchableValue](./-read-only-watchable-value.md)

# ReadOnlyWatchableValue

`interface ReadOnlyWatchableValue<T> : `[`Watchable`](-watchable/index.md)`<`[`T`](-read-only-watchable-value.md#T)`, `[`T`](-read-only-watchable-value.md#T)`, `[`ValueChange`](-value-change/index.md)`<`[`T`](-read-only-watchable-value.md#T)`>>`

A [Watchable](-watchable/index.md) value of type [T](-read-only-watchable-value.md#T) which may not be replaced by the reference holder.

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

### Inheritors

| Name | Summary |
|---|---|
| [WatchableValue](-watchable-value/index.md) | `class WatchableValue<T> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`T`](-watchable-value/index.md#T)`, `[`T`](-watchable-value/index.md#T)`, `[`T`](-watchable-value/index.md#T)`, `[`ValueChange`](-value-change/index.md)`<`[`T`](-watchable-value/index.md#T)`>>, `[`ReadOnlyWatchableValue`](./-read-only-watchable-value.md)`<`[`T`](-watchable-value/index.md#T)`>`<br>A [Watchable](-watchable/index.md) value of [T](-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](watchable-value-of.md) to create. |
