[io.gladed.watchable](../index.md) / [MutableWatchableBase](./index.md)

# MutableWatchableBase

`abstract class MutableWatchableBase<T, M : `[`T`](index.md#T)`, C : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>> : `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`T`](index.md#T)`, `[`M`](index.md#M)`, `[`C`](index.md#C)`>`

Base for implementing a type that is watchable, mutable, and bindable.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `MutableWatchableBase()`<br>Base for implementing a type that is watchable, mutable, and bindable. |

### Properties

| Name | Summary |
|---|---|
| [boundTo](bound-to.md) | `open val boundTo: `[`Watchable`](../-watchable/index.md)`<*, *>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |
| [changes](changes.md) | `val changes: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`C`](index.md#C)`>`<br>Collects changes applied during any mutation of [mutable](mutable.md). |
| [mutable](mutable.md) | `abstract val mutable: `[`M`](index.md#M)<br>The underlying mutable form of the data this object. When changes are applied, [changes](changes.md) must be updated. |
| [value](value.md) | `open val value: `[`T`](index.md#T)<br>Return an immutable copy of the current value of [T](../-watchable/index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [applyBoundChange](apply-bound-change.md) | `abstract fun `[`M`](index.md#M)`.applyBoundChange(change: `[`C`](index.md#C)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Apply all [changes](changes.md) to [M](index.md#M). |
| [bind](bind.md) | `open fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`open fun <T2, C2 : `[`Change`](../-change.md)`<`[`T2`](bind.md#T2)`>> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`T2`](bind.md#T2)`, `[`C2`](bind.md#C2)`>, apply: `[`M`](index.md#M)`.(`[`C2`](bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [doChange](do-change.md) | `fun <U> doChange(func: () -> `[`U`](do-change.md#U)`): `[`U`](do-change.md#U)<br>Run [func](do-change.md#io.gladed.watchable.MutableWatchableBase$doChange(kotlin.Function0((io.gladed.watchable.MutableWatchableBase.doChange.U)))/func) if changes are currently allowed on [immutable](#), or throw if not. |
| [replace](replace.md) | `abstract fun replace(newValue: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Completely replace the contents with a new value, updating changes() as required. |
| [set](set.md) | `open suspend fun set(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Completely replace the contents of this watchable. |
| [subscribe](subscribe.md) | `open fun subscribe(scope: CoroutineScope, consumer: `[`Subscription`](../-subscription/index.md)`<`[`C`](index.md#C)`>.() -> `[`SubscriptionHandle`](../-subscription-handle/index.md)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)<br>Initiate and consume a subscription for changes to this [Watchable](../-watchable/index.md), returning a handle for control over the subscription. |
| [toImmutable](to-immutable.md) | `abstract fun `[`M`](index.md#M)`.toImmutable(): `[`T`](index.md#T)<br>Copy a mutable [M](index.md#M) to an immutable [T](index.md#T). |
| [toInitialChange](to-initial-change.md) | `abstract fun `[`T`](index.md#T)`.toInitialChange(): `[`C`](index.md#C)<br>Given the current state T return a C representing that initial state. |
| [unbind](unbind.md) | `open fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel any existing binding that exists for this object. |
| [use](use.md) | `open suspend fun <U> use(func: `[`M`](index.md#M)`.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)<br>Suspend until [func](../-mutable-watchable/use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](../-mutable-watchable/index.md#M) as desired and returning the result. Note: if currently bound ([isBound](../-mutable-watchable/is-bound.md) returns true), attempts to modify [M](../-mutable-watchable/index.md#M) will throw. |

### Inherited Functions

| Name | Summary |
|---|---|
| [isBound](../-mutable-watchable/is-bound.md) | `open fun isBound(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this object is already bound. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CAPACITY](-c-a-p-a-c-i-t-y.md) | `const val CAPACITY: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableList](../-watchable-list/index.md) | `class WatchableList<T> : `[`MutableWatchableBase`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-watchable-list/index.md#T)`>, `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`T`](../-watchable-list/index.md#T)`>, `[`ListChange`](../-list-change/index.md)`<`[`T`](../-watchable-list/index.md#T)`>>, `[`ReadOnlyWatchableList`](../-read-only-watchable-list.md)`<`[`T`](../-watchable-list/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) wrapper for a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](../watchable-list-of.md) to create. |
| [WatchableMap](../-watchable-map/index.md) | `class WatchableMap<K, V> : `[`MutableWatchableBase`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>, `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>, `[`MapChange`](../-map-change/index.md)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>>, `[`ReadOnlyWatchableMap`](../-read-only-watchable-map.md)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>`<br>A [Watchable](../-watchable/index.md) wrapper for a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](../watchable-map-of.md) to create. |
| [WatchableSet](../-watchable-set/index.md) | `class WatchableSet<T> : `[`MutableWatchableBase`](./index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-watchable-set/index.md#T)`>, `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](../-watchable-set/index.md#T)`>, `[`SetChange`](../-set-change/index.md)`<`[`T`](../-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](../-read-only-watchable-set.md)`<`[`T`](../-watchable-set/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](../watchable-set-of.md) to create. |
| [WatchableValue](../-watchable-value/index.md) | `class WatchableValue<T> : `[`MutableWatchableBase`](./index.md)`<`[`T`](../-watchable-value/index.md#T)`, `[`T`](../-watchable-value/index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-watchable-value/index.md#T)`>>, `[`ReadOnlyWatchableValue`](../-read-only-watchable-value.md)`<`[`T`](../-watchable-value/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) value of [T](../-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create. |
