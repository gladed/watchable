[io.gladed.watchable](../index.md) / [WatchableValue](index.md) / [bind](./bind.md)

# bind

`fun bind(other: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](index.md#T)`>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Overrides [Bindable.bind](../-bindable/bind.md)

Binds this unbound object so that when [other](../-bindable/bind.md#io.gladed.watchable.Bindable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.Bindable.T, io.gladed.watchable.Bindable.C)))/other) changes, it is updated accordingly. This object must not be
modified while bound.

