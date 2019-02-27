[io.gladed.watchable](../index.md) / [WatchableMap](index.md) / [bind](./bind.md)

# bind

`fun bind(other: `[`Watchable`](../-watchable/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>, `[`MapChange`](../-map-change/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Overrides [Bindable.bind](../-bindable/bind.md)

Binds this unbound object so that when [other](../-bindable/bind.md#io.gladed.watchable.Bindable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.Bindable.T, io.gladed.watchable.Bindable.C)))/other) changes, it is updated accordingly. This object must not be
modified while it is bound.

