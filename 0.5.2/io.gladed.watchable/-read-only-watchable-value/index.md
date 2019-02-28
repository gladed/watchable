[io.gladed.watchable](../index.md) / [ReadOnlyWatchableValue](./index.md)

# ReadOnlyWatchableValue

`interface ReadOnlyWatchableValue<T> : `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>`

A value which may not be modified externally but may still be watched for changes.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T) |

### Inherited Functions

| Name | Summary |
|---|---|
| [isActive](../-watchable/is-active.md) | `open fun isActive(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this [Watchable](../-watchable/index.md)'s scope is still active, allowing new [watch](../-watchable/watch.md) requests to succeed. |
| [watch](../-watchable/watch.md) | `open fun CoroutineScope.watch(block: (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes to [block](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/block) using this [CoroutineScope](#) until it terminates, or until the returned [Job](#) is cancelled. The first change will represent the [Watchable](../-watchable/index.md)'s initial value. |
| [watchBatches](../-watchable/watch-batches.md) | `abstract fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver groups of changes to [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) using this [CoroutineScope](#) until it terminates, or until the returned [Job](#) is cancelled. The first change will represent the [Watchable](../-watchable/index.md)'s initial value. |

### Extension Functions

| Name | Summary |
|---|---|
| [watch](../kotlinx.coroutines.-coroutine-scope/watch.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`>, block: (`[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Return a [Job](#) that for the duration of this [CoroutineScope](#) invokes [handler](#) for any changes to [watchable](../kotlinx.coroutines.-coroutine-scope/watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable), starting with its initial state. |
| [watchBatches](../kotlinx.coroutines.-coroutine-scope/watch-batches.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`>> CoroutineScope.watchBatches(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job` |
| [watchableListOf](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableListOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`>`<br>Return a new [WatchableList](../-watchable-list/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#io.gladed.watchable$watchableListOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableListOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableMapOf](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md) | `fun <K, V> CoroutineScope.watchableMapOf(vararg values: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>): `[`WatchableMap`](../-watchable-map/index.md)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>`<br>Return a new [WatchableMap](../-watchable-map/index.md) containing a map of [values](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#io.gladed.watchable$watchableMapOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((kotlin.Pair((io.gladed.watchable.watchableMapOf.K, io.gladed.watchable.watchableMapOf.V)))))/values), watchable on this [CoroutineScope](#). |
| [watchableSetOf](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableSetOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`): `[`WatchableSet`](../-watchable-set/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`>`<br>Return a new [WatchableSet](../-watchable-set/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#io.gladed.watchable$watchableSetOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableSetOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableValueOf](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableValueOf(value: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`>`<br>Return a new [WatchableValue](../-watchable-value/index.md) wrapping [value](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#io.gladed.watchable$watchableValueOf(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.watchableValueOf.T)/value) which may be watched for the duration of the scope. |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableValue](../-watchable-value/index.md) | `class WatchableValue<T> : `[`ReadOnlyWatchableValue`](./index.md)`<`[`T`](../-watchable-value/index.md#T)`>, `[`Bindable`](../-bindable/index.md)`<`[`T`](../-watchable-value/index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-watchable-value/index.md#T)`>>, CoroutineScope`<br>A wrapper for a value which may be watched for changes. |