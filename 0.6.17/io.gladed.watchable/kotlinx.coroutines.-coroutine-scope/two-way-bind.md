[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [twoWayBind](./two-way-bind.md)

# twoWayBind

`fun <M, C : `[`Change`](../-change/index.md)`> CoroutineScope.twoWayBind(left: `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`M`](two-way-bind.md#M)`, `[`C`](two-way-bind.md#C)`>, right: `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`M`](two-way-bind.md#M)`, `[`C`](two-way-bind.md#C)`>): `[`Watcher`](../-watcher/index.md)
`fun <M, M2, C : `[`Change`](../-change/index.md)`, C2 : `[`Change`](../-change/index.md)`> CoroutineScope.twoWayBind(left: `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`M`](two-way-bind.md#M)`, `[`C`](two-way-bind.md#C)`>, right: `[`MutableWatchable`](../-mutable-watchable/index.md)`<`[`M2`](two-way-bind.md#M2)`, `[`C2`](two-way-bind.md#C2)`>, updateLeft: `[`M`](two-way-bind.md#M)`.(`[`C2`](two-way-bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, updateRight: `[`M2`](two-way-bind.md#M2)`.(`[`C`](two-way-bind.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](../-watcher/index.md)

Bi-directionally bind [left](two-way-bind.md#io.gladed.watchable$twoWayBind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.MutableWatchable((io.gladed.watchable.twoWayBind.M, io.gladed.watchable.twoWayBind.C)), io.gladed.watchable.MutableWatchable((io.gladed.watchable.twoWayBind.M, io.gladed.watchable.twoWayBind.C)))/left) and [right](two-way-bind.md#io.gladed.watchable$twoWayBind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.MutableWatchable((io.gladed.watchable.twoWayBind.M, io.gladed.watchable.twoWayBind.C)), io.gladed.watchable.MutableWatchable((io.gladed.watchable.twoWayBind.M, io.gladed.watchable.twoWayBind.C)))/right) together so that any change to one is reflected in the other
(see [MutableWatchable.twoWayBind](../-mutable-watchable/two-way-bind.md)).

