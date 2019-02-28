[io.gladed.watchable](../index.md) / [WatchableValue](./index.md)

# WatchableValue

`class WatchableValue<T> : `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>, `[`Bindable`](../-bindable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>, CoroutineScope`

A wrapper for a value which may be watched for changes.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WatchableValue(coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, initialValue: `[`T`](index.md#T)`)`<br>A wrapper for a value which may be watched for changes. |

### Properties

| Name | Summary |
|---|---|
| [boundTo](bound-to.md) | `val boundTo: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>?`<br>The current binding, if any. |
| [coroutineContext](coroutine-context.md) | `val coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) |
| [value](value.md) | `var value: `[`T`](index.md#T)<br>The current value of the underlying object. |

### Functions

| Name | Summary |
|---|---|
| [bind](bind.md) | `fun bind(other: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object so that when [other](../-bindable/bind.md#io.gladed.watchable.Bindable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.Bindable.T, io.gladed.watchable.Bindable.C)))/other) changes, it is updated accordingly. This object must not be modified while bound. |
| [readOnly](read-only.md) | `fun readOnly(): `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>`<br>Return an unmodifiable form of this [WatchableValue](./index.md). |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [unbind](unbind.md) | `fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel any existing binding that exists for this object. |
| [watchBatches](watch-batches.md) | `fun CoroutineScope.watchBatches(block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver groups of changes to [block](../-watchable/watch-batches.md#io.gladed.watchable.Watchable$watchBatches(kotlinx.coroutines.CoroutineScope, kotlin.Function1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/block) using this [CoroutineScope](#) until it terminates, or until the returned [Job](#) is cancelled. The first change will represent the [Watchable](../-watchable/index.md)'s initial value. |

### Inherited Functions

| Name | Summary |
|---|---|
| [isBound](../-bindable/is-bound.md) | `open fun isBound(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this object is already bound. |

### Extension Functions

| Name | Summary |
|---|---|
| [watch](../kotlinx.coroutines.-coroutine-scope/watch.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`>, block: (`[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Return a [Job](#) that for the duration of this [CoroutineScope](#) invokes [handler](#) for any changes to [watchable](../kotlinx.coroutines.-coroutine-scope/watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable), starting with its initial state. |
| [watchBatches](../kotlinx.coroutines.-coroutine-scope/watch-batches.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`>> CoroutineScope.watchBatches(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job` |
| [watchableListOf](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableListOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`>`<br>Return a new [WatchableList](../-watchable-list/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#io.gladed.watchable$watchableListOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableListOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableMapOf](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md) | `fun <K, V> CoroutineScope.watchableMapOf(vararg values: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>): `[`WatchableMap`](../-watchable-map/index.md)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>`<br>Return a new [WatchableMap](../-watchable-map/index.md) containing a map of [values](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#io.gladed.watchable$watchableMapOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((kotlin.Pair((io.gladed.watchable.watchableMapOf.K, io.gladed.watchable.watchableMapOf.V)))))/values), watchable on this [CoroutineScope](#). |
| [watchableSetOf](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableSetOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`): `[`WatchableSet`](../-watchable-set/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`>`<br>Return a new [WatchableSet](../-watchable-set/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#io.gladed.watchable$watchableSetOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableSetOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableValueOf](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableValueOf(value: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`): `[`WatchableValue`](./index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`>`<br>Return a new [WatchableValue](./index.md) wrapping [value](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#io.gladed.watchable$watchableValueOf(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.watchableValueOf.T)/value) which may be watched for the duration of the scope. |
