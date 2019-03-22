[io.gladed.watchable](../index.md) / [Watchable](index.md) / [watchSimple](./watch-simple.md)

# watchSimple

`open fun watchSimple(scope: CoroutineScope, func: suspend `[`SimpleChange`](../-simple-change/index.md)`<`[`V`](index.md#V)`>.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)

Deliver all changes from this [Watchable](index.md) to [func](watch-simple.md#io.gladed.watchable.Watchable$watchSimple(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.SimpleChange((io.gladed.watchable.Watchable.V)), kotlin.Unit)))/func) receiving [SimpleChange](../-simple-change/index.md) objects.

