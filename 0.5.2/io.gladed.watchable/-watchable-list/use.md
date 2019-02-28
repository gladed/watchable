[io.gladed.watchable](../index.md) / [WatchableList](index.md) / [use](./use.md)

# use

`fun <U> use(func: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`T`](index.md#T)`>.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)

Suspend until [func](use.md#io.gladed.watchable.WatchableList$use(kotlin.Function1((kotlin.collections.MutableList((io.gladed.watchable.WatchableList.T)), io.gladed.watchable.WatchableList.use.U)))/func) can safely execute, reading and/or writing data within the list as desired
and returning [func](use.md#io.gladed.watchable.WatchableList$use(kotlin.Function1((kotlin.collections.MutableList((io.gladed.watchable.WatchableList.T)), io.gladed.watchable.WatchableList.use.U)))/func)'s result. This [WatchableList](index.md) must not be bound ([isBound](../-bindable/is-bound.md) must return false).
[func](use.md#io.gladed.watchable.WatchableList$use(kotlin.Function1((kotlin.collections.MutableList((io.gladed.watchable.WatchableList.T)), io.gladed.watchable.WatchableList.use.U)))/func) should not itself block but simply apply changes and return.

