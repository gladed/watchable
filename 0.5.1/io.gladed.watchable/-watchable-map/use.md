[io.gladed.watchable](../index.md) / [WatchableMap](index.md) / [use](./use.md)

# use

`fun <U> use(func: `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)

Suspend until [func](use.md#io.gladed.watchable.WatchableMap$use(kotlin.Function1((kotlin.collections.MutableMap((io.gladed.watchable.WatchableMap.K, io.gladed.watchable.WatchableMap.V)), io.gladed.watchable.WatchableMap.use.U)))/func) can safely execute, reading and/or writing data within the map as desired
and returning [func](use.md#io.gladed.watchable.WatchableMap$use(kotlin.Function1((kotlin.collections.MutableMap((io.gladed.watchable.WatchableMap.K, io.gladed.watchable.WatchableMap.V)), io.gladed.watchable.WatchableMap.use.U)))/func)'s result. This [WatchableMap](index.md) must not be bound ([isBound](../-bindable/is-bound.md) must return false).
[func](use.md#io.gladed.watchable.WatchableMap$use(kotlin.Function1((kotlin.collections.MutableMap((io.gladed.watchable.WatchableMap.K, io.gladed.watchable.WatchableMap.V)), io.gladed.watchable.WatchableMap.use.U)))/func) should not itself block but simply apply changes and return.

