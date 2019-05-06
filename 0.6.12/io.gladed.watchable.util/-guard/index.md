[io.gladed.watchable.util](../index.md) / [Guard](./index.md)

# Guard

`class Guard<T>`

Protects all access to [item](#) behind a [Mutex](#).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Guard(item: `[`T`](index.md#T)`)`<br>Protects all access to [item](#) behind a [Mutex](#). |

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `suspend operator fun <U> invoke(func: suspend `[`T`](index.md#T)`.() -> `[`U`](invoke.md#U)`): `[`U`](invoke.md#U)<br>Operate directly on the guarded item while holding a [Mutex](#). |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../guarded.md) | `fun <T> `[`T`](../guarded.md#T)`.guarded(): `[`Guard`](./index.md)`<`[`T`](../guarded.md#T)`>`<br>Return [T](../guarded.md#T) surrounded by a [Guard](./index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
