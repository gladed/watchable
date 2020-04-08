[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [waitFor](./wait-for.md)

# waitFor

`suspend fun <C : `[`Change`](../-change/index.md)`, W : `[`Watchable`](../-watchable/index.md)`<`[`C`](wait-for.md#C)`>> CoroutineScope.waitFor(target: `[`W`](wait-for.md#W)`, condition: (`[`W`](wait-for.md#W)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Suspend until [condition](wait-for.md#io.gladed.watchable$waitFor(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.waitFor.W, kotlin.Function1((io.gladed.watchable.waitFor.W, kotlin.Boolean)))/condition) returns true, calling it after each group of changes.

