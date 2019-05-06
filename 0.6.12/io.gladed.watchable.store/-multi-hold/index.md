[io.gladed.watchable.store](../index.md) / [MultiHold](./index.md)

# MultiHold

`class MultiHold<E, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`

A deferred attempt to acquire an instance of [T](index.md#T) on behalf of one or more holding entities,
starting with [entity](#).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `MultiHold(entity: `[`E`](index.md#E)`, value: `[`T`](index.md#T)`, hold: `[`Hold`](../-hold/index.md)`)`<br>Construct a non-deferred version of this [MultiHold](./index.md).`MultiHold(entity: `[`E`](index.md#E)`, hold: Deferred<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`T`](index.md#T)`, `[`Hold`](../-hold/index.md)`>>)`<br>A deferred attempt to acquire an instance of [T](index.md#T) on behalf of one or more holding entities, starting with [entity](#). |

### Properties

| Name | Summary |
|---|---|
| [hold](hold.md) | `val hold: Deferred<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`T`](index.md#T)`, `[`Hold`](../-hold/index.md)`>>` |

### Functions

| Name | Summary |
|---|---|
| [release](release.md) | `suspend fun release(entity: `[`E`](index.md#E)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes a store and returns true if this object can be discarded (e.g. no more stores). |
| [remove](remove.md) | `suspend fun remove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop holding as we delete this item. |
| [reserve](reserve.md) | `suspend fun reserve(store: `[`E`](index.md#E)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stop](stop.md) | `suspend fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop holding. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
