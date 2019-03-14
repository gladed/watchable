# Version History

## 0.6.0

* Remove requirement for each `Watchable` object to have their own `CoroutineScope`.
* Add `group()` API.
* Add sample project.

## 0.5.x

* Add `subscribe` API.
* Allow for a `minPeriod` when batching changes.
* `runBlocking { ... }` no longer hangs forever when containing a watch or bind (#10).
* Improved scope management.
* Removed java synchronization and added suspending `get()` for performance and portability (#16).
* Allow more complex `bind` cases (#17).
* Test and improve concurrent behaviors.
* Package transitioned to `io.gladed.watchable`.
* Revamped rules for safe modification (`use`).
* Initial release
* `WatchableSet`, `WatchableList`, `WatchableMap`, and `WatchableValue`.
* `watch()` and `bind()` methods for all of the above.
