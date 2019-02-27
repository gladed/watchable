[ ![Download](https://api.bintray.com/packages/gladed/watchable/watchable/images/download.svg?version=0.5.1) ](https://bintray.com/gladed/watchable/watchable/0.5.1/link)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gladed_watchable&metric=alert_status)](https://sonarcloud.io/dashboard?id=gladed_watchable)
[![CircleCI](https://circleci.com/gh/gladed/watchable.svg?style=svg)](https://circleci.com/gh/gladed/watchable)
[![CodeCov](https://codecov.io/github/gladed/watchable/coverage.svg?branch=master)](https://codecov.io/github/gladed/watchable)
[![detekt](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://arturbosch.github.io/detekt/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.21-blue.svg)](https://kotlinlang.org/)

# Watchable

This library provides listenable data structures using [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html).

```kotlin
// Inside of CoroutineScope...
val set = watchableSetOf(1, 2)

// Start watching the set for changes
watch(set) {
    println("Got $it")
}

// Make a modification which will notify watchers
set.use { add(3) }

// Output:
//   Got Initial(initial=[1, 2])
//   Got Add(added=3)
```

## Why?

Adding listeners for changes is easy. But it's hard to remember and unregister all of those listeners. If you don't, lots of objects will be kept in memory indefinitely.

`Watchable` objects are bound to the lifetime of the surrounding `CoroutineScope`. When the scope dies, so does its `Watchable`s, along with any `watch` requests made from that scope.

This can be useful in [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html). The data model lives at the center, and everyone points in. If the data model is defined as Watchable objects, then other components (having their own lifecycles) can simply watch for changes and react accordingly, without coupling directly to each other.

# Usage

Add to `build.gradle`:

```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'io.gladed:watchable:0.5.1'
}
```

# Features

## Watch

`WatchableList`, `WatchableSet`, `WatchableMap` wrap the associated types. `WatchableValue` wraps single object values of any type.

You can watch for changes on these objects from the same or a different CoroutineScope with `CoroutineScope.watch(watchable) { ... }`. The supplied block will receive changes according to the watchable's type, starting with an initial state and followed by any changes that occur to the original object.

Every Watchable object requires a `CoroutineContext`. When the context cancels, the Watchable object will stop notifying changes and all watchers will be released.

## Modification

Watchable objects may be modified within a special "use" block, which takes a modifiable form as its receiver:

```kotlin
val set = watchableSetOf(1, 2)
set.use {
    addAll(setOf(2, 3, 4))
    remove(1)
}
```

The `use` block is synchronized to prevent concurrent modification, so it's safe to call from any thread or scope as long as its implementation does not block. Changes are complete when `use` returns.

`WatchableList`, `WatchableSet`, and `WatchableMap` implement their respective collections, but their contents may change at any time.  

## Read-Only Watchable

If you need pass a Watchable that can be watched, but should not be altered, call its `.readOnly()` accessor. This will return a form of the original object that does not have the `.use` method.

## Bind

A `bind` is just a `watch` that connects one watchable to another, so that the receiver automatically gets and applies all changes.

```kotlin
val origin = listOf(4, 5).toWatchableList()

val destination = watchableListOf<Int>()
destination.bind(origin)
watch(destination) {
    println("Got $it")
}
origin.use { add(6) }
// Output:
//   Got Initial(initial=[4, 5])
//   Got Add(added=6)
```

Objects can't be modified after they are bound (but they can be unbound).

# Version History

See [HISTORY.md](HISTORY.md)
