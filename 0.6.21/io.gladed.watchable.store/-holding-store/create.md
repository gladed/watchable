[io.gladed.watchable.store](../index.md) / [HoldingStore](index.md) / [create](./create.md)

# create

`fun create(scope: CoroutineScope): `[`Store`](../-store/index.md)`<`[`T`](index.md#T)`>`

Return a new [Store](../-store/index.md); items accessed by this store will have a corresponding hold (see [createHold](#)) in
effect until the completion of all scopes using the item.

