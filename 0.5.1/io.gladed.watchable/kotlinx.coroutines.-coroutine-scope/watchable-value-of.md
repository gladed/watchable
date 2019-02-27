[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watchableValueOf](./watchable-value-of.md)

# watchableValueOf

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.watchableValueOf(value: `[`T`](watchable-value-of.md#T)`): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](watchable-value-of.md#T)`>`

Return a new [WatchableValue](../-watchable-value/index.md) wrapping [value](watchable-value-of.md#io.gladed.watchable$watchableValueOf(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.watchableValueOf.T)/value) which may be watched for the duration of the scope.

