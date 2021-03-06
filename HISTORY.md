# Version History

## 0.6.22
* Update to Kotlin 1.4.10 and others.

## 0.6.21
* Update dependencies including Kotlin 1.3.71 and others.
* Fixed problems with Cache failures after first backing failure.

## 0.6.20
* Update to Kotlin 1.3.40.
* `Try` for use with `cannot`.
* `sample` now has a full HTML + JSON/REST interface.
* `FileStore` demoted to sample, too use-case-specific.

## 0.6.19
* `lazyGuard` takes suspending func (add on to #97).

## 0.6.18
* Improve guard semantics, add `lazyGuard { }` (#97).

## 0.6.17
* Better semantics for binding a store to a map (#95).
* Save less when watching Container objects (#96).
* Allow manual release of a hold with `HoldingStore.stop` (#87).

## 0.6.16
* `holding` now relies on `HoldBuilder` for more natural composition of holds (#84).
* Add a new way to create holding stores (#86).
* Simple changes now all bear `isInitial` field (#85).
* Improved sample app REST APIs (#89).

## 0.6.15
* `holding` now automatically saves changes to objects implementing `Container` when those objects change (#79).
*  Some internal-use-only objects are correctly marked internal.
* `simple` now has a configurable period time for changes (#80).

## 0.6.14
* Add `twoWayBind` (#76).
* Simplified internal generics.

## 0.6.13
* `Store.toWatchableMap` -> `Store.bind` (#74).

## 0.6.12
* Renamed `Store.delete` to `remove` to make it more map-like.
* Renamed `inflate` to `transform`.
* Expose `MultiHold` for external use.
* Implement `Store.toWatchableMap` and allow objects to implement `Container`.

## 0.6.11
* Fix a memory leak (#68).
* Add more features to sample app.

## 0.6.10
* Add `Watcher.start()`.
* Add `Watchable.waitFor { }`
* Remove `use` in favor of operator `invoke`.
* Remove `suspend` from all operation signatures (`batch`, `watch`, etc).
* Add `isInitial` to all changes (#49).
* Add `Store`, `MemoryStore`, `FileStore`, `Cache`, `HoldingStore`, and `Inflater` for integration with external data.

## 0.6.9
* `Watcher.close` -> `Watcher.stop` to prevent confusion with file close operations
* Fix a problem with watchers on multi-threaded scopes.
* Rename `WatchHandle` to `Watcher` and simplified `close()` semantics.
* Remove `value` and add suspend operators to `WatchableList`, `WatchableSet`, and `WatchableMap` for easier modification.
* Support for `INLINE` vs `IMMEDIATE` watch periods.
* Restored access to removed values in change notifications
* Simplified generic types
* Eliminated some use of coroutine APIs marked "Obsolete".
* Changed `watchSimple` to `simple`

## 0.6.x
* Closed `WatchHandle` but retained objects no longer leak memory.
* `watchSimple` allows for simplified handling of add/removes when efficiency and completeness are not as important.
* `MutableWatchable` objects support suspending modifiers for one-shot operations like add/remove/clear.
* Support `null` in list, value, and set.
* Support equality tests for value as much as possible.
* Remove `subscribe` APIs (too dangerous)
* Handle coming back from `watch` etc support `close` which flushes current contents.
* Replace suspending `get()` with `value` for direct immutable access without suspension.
* Replace read-only collection implementations for Map, List, and Set.
* Fix a problem with `batch` failing to consume, slowing down `use`.
* Remove requirement for each `Watchable` object to have their own `CoroutineScope`.
* Add `group()` API.
* Add sample project.

## 0.5.x

* Add `subscribe` API.
* Allow for a `minPeriod` when batching changes.
* `runBlocking { ... }` no longer hangs forever when containing a watch or bind (#10).
* Improve scope management.
* Remove java synchronization and add suspending `get()` for performance and portability (#16).
* Allow more complex `bind` cases (#17).
* Test and improve concurrent behaviors.
* Package transitioned to `io.gladed.watchable`.
* Revamp rules for safe modification (`use`).
* Initial release
* `WatchableSet`, `WatchableList`, `WatchableMap`, and `WatchableValue`.
* `watch()` and `bind()` methods for all of the above.
