[io.gladed.watchable.util](../index.md) / [Try](./index.md)

# Try

`sealed class Try<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`

The result of a suspending attempt to do something that might not be possible (e.g. could throw [Cannot](../-cannot/index.md)).

### Types

| Name | Summary |
|---|---|
| [Fail](-fail/index.md) | `data class Fail<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Try`](./index.md)`<`[`T`](-fail/index.md#T)`>` |
| [Pass](-pass/index.md) | `data class Pass<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Try`](./index.md)`<`[`T`](-pass/index.md#T)`>` |

### Properties

| Name | Summary |
|---|---|
| [failOrNull](fail-or-null.md) | `abstract val failOrNull: `[`Cannot`](../-cannot/index.md)`?` |
| [passOrNull](pass-or-null.md) | `abstract val passOrNull: `[`T`](index.md#T)`?` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `suspend operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> invoke(func: suspend () -> `[`T`](invoke.md#T)`): `[`Try`](./index.md)`<`[`T`](invoke.md#T)`>` |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../guard.md) | `fun <T> `[`T`](../guard.md#T)`.guard(): `[`Guard`](../-guard/index.md)`<`[`T`](../guard.md#T)`>`<br>Return [T](../guard.md#T) surrounded by a [Guard](../-guard/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [Fail](-fail/index.md) | `data class Fail<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Try`](./index.md)`<`[`T`](-fail/index.md#T)`>` |
| [Pass](-pass/index.md) | `data class Pass<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Try`](./index.md)`<`[`T`](-pass/index.md#T)`>` |
