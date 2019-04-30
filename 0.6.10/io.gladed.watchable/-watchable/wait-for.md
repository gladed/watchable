[io.gladed.watchable](../index.md) / [Watchable](index.md) / [waitFor](./wait-for.md)

# waitFor

`open suspend fun waitFor(scope: CoroutineScope, func: () -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Suspend, calling [func](wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) as changes arrive, and return when [func](wait-for.md#io.gladed.watchable.Watchable$waitFor(kotlinx.coroutines.CoroutineScope, kotlin.Function0((kotlin.Boolean)))/func) returns true.

