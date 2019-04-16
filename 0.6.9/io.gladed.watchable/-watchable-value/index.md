[io.gladed.watchable](../index.md) / [WatchableValue](./index.md)

# WatchableValue

`interface WatchableValue<T> : `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`MutableValue`](../-mutable-value/index.md)`<`[`T`](index.md#T)`>, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>, `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>`

A [Watchable](../-watchable/index.md) value of [T](index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create.

### Inherited Properties

| Name | Summary |
|---|---|
| [boundTo](../-mutable-watchable/bound-to.md) | `abstract val boundTo: `[`Watchable`](../-watchable/index.md)`<*>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |
| [value](../-read-only-watchable-value/value.md) | `abstract val value: `[`T`](../-read-only-watchable-value/index.md#T)<br>Direct access to the current value inside the container. |

### Functions

| Name | Summary |
|---|---|
| [readOnly](read-only.md) | `abstract fun readOnly(): `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>`<br>Return an unmodifiable form of this [WatchableSet](../-watchable-set/index.md). |
| [set](set.md) | `abstract suspend fun set(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Replace the old value with [value](set.md#io.gladed.watchable.WatchableValue$set(io.gladed.watchable.WatchableValue.T)/value). |

### Inherited Functions

| Name | Summary |
|---|---|
| [bind](../-mutable-watchable/bind.md) | `abstract suspend fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C`](../-mutable-watchable/index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`abstract suspend fun <C2 : `[`Change`](../-change.md)`> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C2`](../-mutable-watchable/bind.md#C2)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, apply: `[`M`](../-mutable-watchable/index.md#M)`.(`[`C2`](../-mutable-watchable/bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [clear](../-mutable-watchable/clear.md) | `abstract suspend fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove all items. |
| [isBound](../-mutable-watchable/is-bound.md) | `open fun isBound(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this object is already bound. |
| [unbind](../-mutable-watchable/unbind.md) | `abstract fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove any existing binding for this object. |
| [use](../-mutable-watchable/use.md) | `abstract suspend fun <U> use(func: `[`M`](../-mutable-watchable/index.md#M)`.() -> `[`U`](../-mutable-watchable/use.md#U)`): `[`U`](../-mutable-watchable/use.md#U)<br>Suspend until [func](../-mutable-watchable/use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](../-mutable-watchable/index.md#M) as desired and returning the result. Note: if currently bound ([isBound](../-mutable-watchable/is-bound.md) returns true), attempts to modify [M](../-mutable-watchable/index.md#M) will throw. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](./index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
