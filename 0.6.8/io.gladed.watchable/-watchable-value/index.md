[io.gladed.watchable](../index.md) / [WatchableValue](./index.md)

# WatchableValue

`class WatchableValue<T> : `[`MutableWatchableBase`](../-mutable-watchable-base/index.md)`<`[`Value`](../-value/index.md)`<`[`T`](index.md#T)`>, `[`T`](index.md#T)`, `[`MutableValue`](../-mutable-value/index.md)`<`[`T`](index.md#T)`>, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>, `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>`

A [Watchable](../-watchable/index.md) value of [T](index.md#T) which may also be replaced or bound. Use [watchableValueOf](../watchable-value-of.md) to create.

### Properties

| Name | Summary |
|---|---|
| [immutable](immutable.md) | `var immutable: `[`Value`](../-value/index.md)`<`[`T`](index.md#T)`>`<br>The current immutable [T](../-mutable-watchable-base/index.md#T) form of [M](../-mutable-watchable-base/index.md#M). |
| [mutable](mutable.md) | `var mutable: `[`MutableValue`](../-mutable-value/index.md)`<`[`T`](index.md#T)`>`<br>The underlying mutable form of the data this object. When changes are applied, [changes](#) must be updated. |
| [value](value.md) | `val value: `[`T`](index.md#T)<br>The currently contained value. |

### Inherited Properties

| Name | Summary |
|---|---|
| [boundTo](../-mutable-watchable-base/bound-to.md) | `open val boundTo: `[`Watchable`](../-watchable/index.md)`<*>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |

### Functions

| Name | Summary |
|---|---|
| [applyBoundChange](apply-bound-change.md) | `fun `[`MutableValue`](../-mutable-value/index.md)`<`[`T`](index.md#T)`>.applyBoundChange(change: `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Apply [change](../-mutable-watchable-base/apply-bound-change.md#io.gladed.watchable.MutableWatchableBase$applyBoundChange(io.gladed.watchable.MutableWatchableBase.M, io.gladed.watchable.MutableWatchableBase.C)/change) to [M](../-mutable-watchable-base/index.md#M). |
| [clear](clear.md) | `suspend fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove all items. |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [readOnly](read-only.md) | `fun readOnly(): `[`ReadOnlyWatchableValue`](../-read-only-watchable-value/index.md)`<`[`T`](index.md#T)`>`<br>Return an unmodifiable form of this [WatchableSet](../-watchable-set/index.md). |
| [set](set.md) | `suspend fun set(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Insert a new value, replacing the old one. |
| [toImmutable](to-immutable.md) | `fun `[`MutableValue`](../-mutable-value/index.md)`<`[`T`](index.md#T)`>.toImmutable(): `[`Value`](../-value/index.md)`<`[`T`](index.md#T)`>`<br>Copy a mutable [M](../-mutable-watchable-base/index.md#M) to an immutable [T](../-mutable-watchable-base/index.md#T). |
| [toInitialChange](to-initial-change.md) | `fun `[`Value`](../-value/index.md)`<`[`T`](index.md#T)`>.toInitialChange(): `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>`<br>Given the current state [T](../-mutable-watchable-base/index.md#T) return [C](../-mutable-watchable-base/index.md#C) representing the initial state, if any. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [bind](../-mutable-watchable-base/bind.md) | `open suspend fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C`](../-mutable-watchable-base/index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`open suspend fun <C2 : `[`Change`](../-change.md)`> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C2`](../-mutable-watchable-base/bind.md#C2)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, apply: `[`M`](../-mutable-watchable-base/index.md#M)`.(`[`C2`](../-mutable-watchable-base/bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [doChange](../-mutable-watchable-base/do-change.md) | `fun <U> doChange(func: () -> `[`U`](../-mutable-watchable-base/do-change.md#U)`): `[`U`](../-mutable-watchable-base/do-change.md#U)<br>Run [func](../-mutable-watchable-base/do-change.md#io.gladed.watchable.MutableWatchableBase$doChange(kotlin.Function0((io.gladed.watchable.MutableWatchableBase.doChange.U)))/func) if changes are currently allowed, or throw if not. |
| [getInitialChange](../-mutable-watchable-base/get-initial-change.md) | `open fun getInitialChange(): `[`C`](../-mutable-watchable-base/index.md#C)`?`<br>Return the initial change that a new watcher should receive, if any |
| [record](../-mutable-watchable-base/record.md) | `fun record(change: `[`C`](../-mutable-watchable-base/index.md#C)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Record the latest change. |
| [unbind](../-mutable-watchable-base/unbind.md) | `open fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove any existing binding for this object. |
| [use](../-mutable-watchable-base/use.md) | `open suspend fun <U> use(func: `[`M`](../-mutable-watchable-base/index.md#M)`.() -> `[`U`](../-mutable-watchable-base/use.md#U)`): `[`U`](../-mutable-watchable-base/use.md#U)<br>Suspend until [func](../-mutable-watchable/use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](../-mutable-watchable/index.md#M) as desired and returning the result. Note: if currently bound ([isBound](../-mutable-watchable/is-bound.md) returns true), attempts to modify [M](../-mutable-watchable/index.md#M) will throw. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](./index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
