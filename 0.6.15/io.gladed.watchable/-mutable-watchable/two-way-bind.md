[io.gladed.watchable](../index.md) / [MutableWatchable](index.md) / [twoWayBind](./two-way-bind.md)

# twoWayBind

`abstract fun twoWayBind(scope: CoroutineScope, other: `[`MutableWatchable`](index.md)`<`[`M`](index.md#M)`, `[`C`](index.md#C)`>): `[`Watcher`](../-watcher/index.md)
`abstract fun <M2, C2 : `[`Change`](../-change/index.md)`> twoWayBind(scope: CoroutineScope, other: `[`MutableWatchable`](index.md)`<`[`M2`](two-way-bind.md#M2)`, `[`C2`](two-way-bind.md#C2)`>, update: `[`M`](index.md#M)`.(`[`C2`](two-way-bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, updateOther: `[`M2`](two-way-bind.md#M2)`.(`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Bind [other](two-way-bind.md#io.gladed.watchable.MutableWatchable$twoWayBind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.MutableWatchable((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.C)))/other) to this so that any change in either object is reflected in the other.

