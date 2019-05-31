[io.gladed.watchable.store](../index.md) / [Transformer](./index.md)

# Transformer

`interface Transformer<S : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`

Convert between source ([S](index.md#S)) and target ([T](index.md#T)) forms of an object.

### Functions

| Name | Summary |
|---|---|
| [fromTarget](from-target.md) | `abstract fun fromTarget(value: `[`T`](index.md#T)`): `[`S`](index.md#S)<br>Convert an instance of [T](index.md#T) to [S](index.md#S). |
| [plus](plus.md) | `open operator fun <T2 : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> plus(other: `[`Transformer`](./index.md)`<`[`T`](index.md#T)`, `[`T2`](plus.md#T2)`>): `[`Transformer`](./index.md)`<`[`S`](index.md#S)`, `[`T2`](plus.md#T2)`>`<br>Combine this [Transformer](./index.md) with [other](plus.md#io.gladed.watchable.store.Transformer$plus(io.gladed.watchable.store.Transformer((io.gladed.watchable.store.Transformer.T, io.gladed.watchable.store.Transformer.plus.T2)))/other) to produce a transitive [Transformer](./index.md). |
| [toTarget](to-target.md) | `abstract fun toTarget(value: `[`S`](index.md#S)`): `[`T`](index.md#T)<br>Convert an instance of [S](index.md#S) to [T](index.md#T). |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
