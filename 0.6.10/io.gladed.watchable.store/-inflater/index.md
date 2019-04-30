[io.gladed.watchable.store](../index.md) / [Inflater](./index.md)

# Inflater

`interface Inflater<A : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, B : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`

Convert between "deflated" ([A](index.md#A)) and "inflated" ([B](index.md#B)) forms of an object.

### Functions

| Name | Summary |
|---|---|
| [deflate](deflate.md) | `abstract fun deflate(value: `[`B`](index.md#B)`): `[`A`](index.md#A)<br>Convert an instance of [B](index.md#B) to [A](index.md#A). |
| [inflate](inflate.md) | `abstract fun inflate(value: `[`A`](index.md#A)`): `[`B`](index.md#B)<br>Convert an instance of [A](index.md#A) to [B](index.md#B). |
| [plus](plus.md) | `open operator fun <C : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> plus(other: `[`Inflater`](./index.md)`<`[`B`](index.md#B)`, `[`C`](plus.md#C)`>): `[`Inflater`](./index.md)`<`[`A`](index.md#A)`, `[`C`](plus.md#C)`>`<br>Combine this [Inflater](./index.md) with [other](plus.md#io.gladed.watchable.store.Inflater$plus(io.gladed.watchable.store.Inflater((io.gladed.watchable.store.Inflater.B, io.gladed.watchable.store.Inflater.plus.C)))/other) to produce a transitive [Inflater](./index.md). |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
