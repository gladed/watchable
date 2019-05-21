[io.gladed.watchable](../index.md) / [MutableWatchable](./index.md)

# MutableWatchable

`interface MutableWatchable<M, C : `[`Change`](../-change/index.md)`> : `[`Watchable`](../-watchable/index.md)`<`[`C`](index.md#C)`>`

A [Watchable](../-watchable/index.md) containing a mutable object of type [M](index.md#M), which can both generate and accept changes of type [C](index.md#C).

### Properties

| Name | Summary |
|---|---|
| [boundTo](bound-to.md) | `abstract val boundTo: `[`Watchable`](../-watchable/index.md)`<*>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |

### Functions

| Name | Summary |
|---|---|
| [bind](bind.md) | `abstract fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C`](index.md#C)`>): `[`Watcher`](../-watcher/index.md)<br>Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`abstract fun <C2 : `[`Change`](../-change/index.md)`> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C2`](bind.md#C2)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, apply: `[`M`](index.md#M)`.(`[`C2`](bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [clear](clear.md) | `abstract suspend fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove all items. |
| [invoke](invoke.md) | `abstract suspend operator fun <U> invoke(func: `[`M`](index.md#M)`.() -> `[`U`](invoke.md#U)`): `[`U`](invoke.md#U)<br>Suspend until [func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func) can safely execute on the mutable form [M](index.md#M) of this watchable, returning [func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func)'s result. [func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func) must not block or return the mutable form outside of this routine. |
| [isBound](is-bound.md) | `open fun isBound(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this object is already bound. |
| [readOnly](read-only.md) | `abstract fun readOnly(): `[`Watchable`](../-watchable/index.md)`<`[`C`](index.md#C)`>`<br>Return a read-only form of this [MutableWatchable](./index.md). |
| [twoWayBind](two-way-bind.md) | `abstract fun twoWayBind(scope: CoroutineScope, other: `[`MutableWatchable`](./index.md)`<`[`M`](index.md#M)`, `[`C`](index.md#C)`>): `[`Watcher`](../-watcher/index.md)<br>`abstract fun <M2, C2 : `[`Change`](../-change/index.md)`> twoWayBind(scope: CoroutineScope, other: `[`MutableWatchable`](./index.md)`<`[`M2`](two-way-bind.md#M2)`, `[`C2`](two-way-bind.md#C2)`>, update: `[`M`](index.md#M)`.(`[`C2`](two-way-bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, updateOther: `[`M2`](two-way-bind.md#M2)`.(`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Bind [other](two-way-bind.md#io.gladed.watchable.MutableWatchable$twoWayBind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.MutableWatchable((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.C)))/other) to this so that any change in either object is reflected in the other. |
| [unbind](unbind.md) | `abstract fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove any existing binding for this object. |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](../-watchable/batch.md) | `abstract fun batch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects until [scope](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [waitFor](../-watchable/wait-for.md) | `open suspend fun waitFor(scope: CoroutineScope, func: () -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend, calling [func](../-watchable/wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) as changes arrive, and return when [func](../-watchable/wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) returns true. |
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, func: suspend (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)<br>Deliver all changes from this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects until [scope](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.coroutines.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableList](../-watchable-list/index.md) | `interface WatchableList<T> : `[`MutableWatchable`](./index.md)`<`[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`T`](../-watchable-list/index.md#T)`>, `[`ListChange`](../-list-change/index.md)`<`[`T`](../-watchable-list/index.md#T)`>>, `[`ReadOnlyWatchableList`](../-read-only-watchable-list.md)`<`[`T`](../-watchable-list/index.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](../watchable-list-of.md) to create. |
| [WatchableMap](../-watchable-map/index.md) | `interface WatchableMap<K, V> : `[`MutableWatchable`](./index.md)`<`[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>, `[`MapChange`](../-map-change/index.md)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>>, `[`ReadOnlyWatchableMap`](../-read-only-watchable-map.md)`<`[`K`](../-watchable-map/index.md#K)`, `[`V`](../-watchable-map/index.md#V)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](../watchable-map-of.md) to create. |
| [WatchableSet](../-watchable-set/index.md) | `interface WatchableSet<T> : `[`MutableWatchable`](./index.md)`<`[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](../-watchable-set/index.md#T)`>, `[`SetChange`](../-set-change/index.md)`<`[`T`](../-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](../-read-only-watchable-set.md)`<`[`T`](../-watchable-set/index.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](../watchable-set-of.md) to create. |
| [WatchableValue](../-watchable-value/index.md) | `interface WatchableValue<T> : `[`MutableWatchable`](./index.md)`<`[`MutableValue`](../-mutable-value/index.md)`<`[`T`](../-watchable-value/index.md#T)`>, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-watchable-value/index.md#T)`>>, `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](../-watchable-value/index.md#T)`>`<br>A [Watchable](../-watchable/index.md) value of [T](../-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create. |