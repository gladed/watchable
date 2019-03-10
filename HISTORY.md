# Version History

## 0.5.5

* Allow `subscribe`
* Allow for a `minPeriod` when batching changes.

## 0.5.4

* `runBlocking { ... }` no longer hangs forever when containing a watch or bind (#10).
* Improved scope management.

## 0.5.3

* Removed java synchronization and added suspending `get()` for performance and portability (#16).
* Allow more complex `bind` cases (#17).

## 0.5.2

* Test and improve concurrent behaviors.

## 0.5.1

* Package transitioned to `io.gladed.watchable`.
* Revamped rules for safe modification (`use`).

## 0.5.0

* Initial release
* `WatchableSet`, `WatchableList`, `WatchableMap`, and `WatchableValue`.
* `watch()` and `bind()` methods for all of the above.
