[io.gladed.watchable](../index.md) / [WatchableSet](index.md) / [use](./use.md)

# use

`fun <U> use(func: `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](index.md#T)`>.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)

Suspend until [func](use.md#io.gladed.watchable.WatchableSet$use(kotlin.Function1((kotlin.collections.MutableSet((io.gladed.watchable.WatchableSet.T)), io.gladed.watchable.WatchableSet.use.U)))/func) can safely execute, reading and/or writing data within the set as desired
and returning [func](use.md#io.gladed.watchable.WatchableSet$use(kotlin.Function1((kotlin.collections.MutableSet((io.gladed.watchable.WatchableSet.T)), io.gladed.watchable.WatchableSet.use.U)))/func)'s result. This [WatchableSet](index.md) must not be bound ([isBound](../-bindable/is-bound.md) must return false).
[func](use.md#io.gladed.watchable.WatchableSet$use(kotlin.Function1((kotlin.collections.MutableSet((io.gladed.watchable.WatchableSet.T)), io.gladed.watchable.WatchableSet.use.U)))/func) should not itself block but simply apply changes and return.

