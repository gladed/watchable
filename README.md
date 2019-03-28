[![Download](https://api.bintray.com/packages/gladed/watchable/watchable/images/download.svg?version=0.6.7)](https://bintray.com/gladed/watchable/watchable/0.6.7/link)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gladed_watchable&metric=alert_status)](https://sonarcloud.io/dashboard?id=gladed_watchable)
[![CircleCI](https://circleci.com/gh/gladed/watchable.svg?style=svg)](https://circleci.com/gh/gladed/watchable)
[![CodeCov](https://codecov.io/github/gladed/watchable/coverage.svg?branch=master)](https://codecov.io/github/gladed/watchable)
[![detekt](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://arturbosch.github.io/detekt/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.21-blue.svg)](https://kotlinlang.org/)
[![API Docs](https://img.shields.io/badge/API_Docs-latest-purple.svg)](https://gladed.github.io/watchable/latest/io.gladed.watchable/)

# Watchable

This library provides lock-free, concurrent, listenable data structures using [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html).

```kotlin
// Inside of CoroutineScope...
val set = watchableSetOf(1, 2)

// Start watching the set for changes
watch(set) { println("Got $it") }

// ...Later, make a modification which will notify watchers
set.use { add(3) }

// Output:
//   Got SetChange.Initial(initial=[1, 2])"
//   Got SetChange.Add(added=3)
```

## Why?

Sometimes, you want a data structure that can be shared between objects and listen for changes to that data. But it's hard to remember and unregister all of those listeners. If you don't, you'll leak memory.

`Watchable` solves this by allowing operations (like `watch` and`bind`) which are limited to the lifetime of the `CoroutineScope` where they are created. When the scope completes, its watchable operations also stop and cleaned up automatically.

This is useful in [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) designs, in which the data model lives at the center, and everyone depends on it. If the data model is defined in terms of `Watchable` objects, then other components (having their own lifecycles) can simply watch for changes and react accordingly, without coupling directly to each other.

# Usage

Add to `build.gradle`:

```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'io.gladed:watchable:0.6.7'
}
```

# Features

## Watchable Data Types

[`WatchableList`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-list/), [`WatchableSet`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-set/), and [`WatchableMap`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-map/) allow access to wrapped List, Set, and Map data. [`WatchableValue`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-value/) wraps a single object value of any type.

```kotlin
val list = watchableListOf(1, 2, 3)
val map = watchableMapOf(4 to "four")
val set = watchableSetOf(5.0, 6.0)
val value = watchableValueOf(URI.create("https://github.com"))
```

Each data type can be accessed, modified, watched, bound, etc. as described below.

## Reading Data

You can obtain a read-only, immutable copy of the underlying data with [`value`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/value.html). In the case of `WatchableList`, `WatchableSet`, and `WatchableMap` can also be treated as read-only views of the underlying content, but be warned that this content may change at any time.

```kotlin
val list = watchableListOf(1, 2, 3)
println(list) // Prints WatchableList([1, 2, 3])
println(list.value) // Prints [1, 2, 3]
```

## Modifying Contents

Any [`MutableWatchable`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/) can be modified with [`use`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/use.html), which receives a temporary, modifiable form of the underlying data. Any changes to this data are captured and delivered to watchers.

```kotlin
val list = watchableListOf(1, 2)
list.use { add(3) }
println(list.value) // Prints [1, 2, 3]
```  

NOTE: Your `use` function must NOT block.

## Watching for Changes

You can watch any `Watchable` for changes from any `CoroutineScope` using [`watch`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/watch.html):

```kotlin
val set = watchableSetOf(1, 2)
watch(set) { println(it) } // Prints: Initial(initial=[1, 2])
set.use { add(3) } // Prints: Add(added=3)
```

## Binding

A [`bind`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/bind.html) is just a `watch` that connects one watchable of the same type to another, so that the destination automatically receives all changes from an origin.

```kotlin
val from = listOf(4, 5).toWatchableList()
val into = watchableListOf<Int>()
bind(into, from)
// Eventually, destination will match origin, and will stay in sync with any further changes to origin.
```

While bound, a watchable cannot be independently modified, and attempts to do so in `use` will throw.

Complex binds are possible in which changes are received and may be arbitrarily mapped into changes on a bound item. See the `apply` parameter.

## Batching

It's possible to listen for lists of changes, collected and delivered in-order periodically. See the documentation for [batch](https://gladed.github.io/watchable/latest/io.gladed.watchable/kotlinx.coroutines.-coroutine-scope/batch.html), especially the `minPeriod` parameter.

```kotlin
val list = listOf(4, 5).toWatchableList()
batch(list, 50) { println(it) } // Prints: [Initial(initial=[4, 5])]
list.use { add(6); add(7) } // Prints: [Add(index=2, added=6), Add(index=3, added=7)]
```

## Read-Only Watchables

You can use a `MutableWatchable`'s `.readOnly()` function to return a `Watchable` copy, which cannot be changed externally. The copy may still be watched normally.

## Grouping

You can [`group`](https://gladed.github.io/watchable/latest/io.gladed.watchable/group.html) several watchables into a [`WatchableGroup`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-group/) so that you receive changes for both:

```kotlin
val list = listOf(4).toWatchableList()
val set = setOf("a").toWatchableSet()

watch(group(set, list)) { println(it) }
// Prints:
//   GroupChange(watchable=WatchableSet(), change=Initial(initial=[a]))
//   GroupChange(watchable=WatchableList(), change=Initial(initial=[4]))

list.use { add(6) }
set.use { add("b") }
// Prints:
//   GroupChange(watchable=WatchableList(), change=Add(index=1, added=6))
//   GroupChange(watchable=WatchableSet(), change=Add(added=b))
```

## Object Lifetime

The initiating `CoroutineScope` lifetime is respected. Operations such as `watch` or `bind` will automatically stop when the initiating scope completes. No additional cleanup code is required.

Some operations return return a `SubscriptionHandle` which can be used to stop the operation with more granular control. For example, calling `close` on the [`SubscriptionHandle`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-subscription-handle/) returned by `watch` allows any pending changes to be processed to completion.

# Sample

See the [Sample Project](/sample) for some ideas on how this could be integrated into a project.

# Version History

See [HISTORY.md](HISTORY.md)
