[io.gladed.watchable](../index.md) / [Bindable](index.md) / [bind](./bind.md)

# bind

`abstract fun bind(other: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Binds this unbound object so that when [other](bind.md#io.gladed.watchable.Bindable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.Bindable.T, io.gladed.watchable.Bindable.C)))/other) changes, it is updated accordingly. This object must not be
modified while it is bound.

