[io.gladed.watchable](../index.md) / [WatchableDelegate](./index.md)

# WatchableDelegate

`abstract class WatchableDelegate<T, C : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>> : CoroutineScope`

Common internal implementations for watchable + bindable functions.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WatchableDelegate(coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, owner: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>)`<br>Common internal implementations for watchable + bindable functions. |

### Properties

| Name | Summary |
|---|---|
| [boundTo](bound-to.md) | `var boundTo: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>?` |
| [coroutineContext](coroutine-context.md) | `open val coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html) |
| [initialChange](initial-change.md) | `abstract val initialChange: `[`C`](index.md#C)<br>A change reflecting the current value of this collection. (New watchers receive this). |

### Functions

| Name | Summary |
|---|---|
| [bind](bind.md) | `fun bind(other: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [checkChange](check-change.md) | `fun checkChange(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Throw if this is not a good time to mutate the owner object (because it's bound and not currently processing binding-related changes). |
| [onBoundChanges](on-bound-changes.md) | `abstract fun onBoundChanges(changes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Process [changes](on-bound-changes.md#io.gladed.watchable.WatchableDelegate$onBoundChanges(kotlin.collections.List((io.gladed.watchable.WatchableDelegate.C)))/changes), applying them to [owner](#). |
| [send](send.md) | `suspend fun send(changes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Deliver changes to watchers if possible. |
| [unbind](unbind.md) | `fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [watchOwnerBatch](watch-owner-batch.md) | `fun watchOwnerBatch(scope: CoroutineScope, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job` |

### Extension Functions

| Name | Summary |
|---|---|
| [watch](../kotlinx.coroutines.-coroutine-scope/watch.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`>, block: (`[`C`](../kotlinx.coroutines.-coroutine-scope/watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Return a [Job](#) that for the duration of this [CoroutineScope](#) invokes [handler](#) for any changes to [watchable](../kotlinx.coroutines.-coroutine-scope/watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable), starting with its initial state. |
| [watchBatches](../kotlinx.coroutines.-coroutine-scope/watch-batches.md) | `fun <T, C : `[`Change`](../-change.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`>> CoroutineScope.watchBatches(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#T)`, `[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>, block: (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../kotlinx.coroutines.-coroutine-scope/watch-batches.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job` |
| [watchableListOf](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableListOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#T)`>`<br>Return a new [WatchableList](../-watchable-list/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-list-of.md#io.gladed.watchable$watchableListOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableListOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableMapOf](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md) | `fun <K, V> CoroutineScope.watchableMapOf(vararg values: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>): `[`WatchableMap`](../-watchable-map/index.md)`<`[`K`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#K)`, `[`V`](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#V)`>`<br>Return a new [WatchableMap](../-watchable-map/index.md) containing a map of [values](../kotlinx.coroutines.-coroutine-scope/watchable-map-of.md#io.gladed.watchable$watchableMapOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((kotlin.Pair((io.gladed.watchable.watchableMapOf.K, io.gladed.watchable.watchableMapOf.V)))))/values), watchable on this [CoroutineScope](#). |
| [watchableSetOf](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableSetOf(vararg values: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`): `[`WatchableSet`](../-watchable-set/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#T)`>`<br>Return a new [WatchableSet](../-watchable-set/index.md) containing [values](../kotlinx.coroutines.-coroutine-scope/watchable-set-of.md#io.gladed.watchable$watchableSetOf(kotlinx.coroutines.CoroutineScope, kotlin.Array((io.gladed.watchable.watchableSetOf.T)))/values), watchable on this [CoroutineScope](#). |
| [watchableValueOf](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableValueOf(value: `[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#T)`>`<br>Return a new [WatchableValue](../-watchable-value/index.md) wrapping [value](../kotlinx.coroutines.-coroutine-scope/watchable-value-of.md#io.gladed.watchable$watchableValueOf(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.watchableValueOf.T)/value) which may be watched for the duration of the scope. |
