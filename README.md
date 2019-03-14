[ ![Download](https://api.bintray.com/packages/gladed/watchable/watchable/images/download.svg?version=0.6.0) ](https://bintray.com/gladed/watchable/watchable/0.6.0/link)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gladed_watchable&metric=alert_status)](https://sonarcloud.io/dashboard?id=gladed_watchable)
[![CircleCI](https://circleci.com/gh/gladed/watchable.svg?style=svg)](https://circleci.com/gh/gladed/watchable)
[![CodeCov](https://codecov.io/github/gladed/watchable/coverage.svg?branch=master)](https://codecov.io/github/gladed/watchable)
[![detekt](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://arturbosch.github.io/detekt/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.21-blue.svg)](https://kotlinlang.org/)
[![API Docs](https://img.shields.io/badge/API_Docs-0.6.0-purple.svg)](https://gladed.github.io/watchable/0.6.0/io.gladed.watchable/)

# Watchable

This library provides listenable data structures using [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html).

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
    compile 'io.gladed:watchable:0.6.0'
}
```

# Features

## Watchable Data Types

[`WatchableList`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-list/), [`WatchableSet`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-set/), and [`WatchableMap`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-map/) allow access to wrapped List, Set, and Map data. [`WatchableValue`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-value/) wraps a single object value of any type.

These types are created on a CoroutineScope with `watchable*Of(...)`. For example:

```kotlin
class MyClass : CoroutineScope {
    val list = watchableListOf(1, 2, 3)
    val map = watchableMapOf(4 to "four")
    val set = watchableSetOf(5.0, 6.0)
    val value = watchableValueOf(URI.create("https://github.com"))
```

Each data type can be accessed, modified, watched, and bound. 

## Reading Data

You can obtain a read-only, immutable copy of the underlying data using [`get()`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/get.html). This may suspend for a short time while other coroutines complete modifications of the data.

```kotlin
val list = watchableListOf(1, 2, 3)
println(list.get()) // Prints [1, 2, 3]
```

## Modifying Contents

Any [`MutableWatchable`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/) can be modified with [`use`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/use.html), which receives a modifiable form of the underlying data:

```kotlin
val list = watchableListOf(1, 2)
list.use { add(3) }
println(list.get()) // Prints [1, 2, 3]
```  

If other coroutines are already using the object, `use()` will suspend until they are done, then execute your code. In this way, all modifications run sequentially.

## Watching for Changes

You can watch any `Watchable` for changes from any `CoroutineScope` using [watch](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/watch.html)

```kotlin
val set = watchableSetOf(1, 2)
set.watch { println(it) } // Prints: Initial(initial=[1, 2])
set.use { add(3) } // Prints: Add(added=3)
```

## Read-Only Watchable

You can use a `MutableWatchable`'s `.readOnly()` function to return a copy indicating it must not be changed externally. The copy may still be watched normally.

## Binding

A [`bind`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/bind.html) is just a `watch` that connects one watchable to another, so that the destination automatically receives all changes from an origin.

```kotlin
val origin = listOf(4, 5).toWatchableList()
val destination = watchableListOf<Int>()
destination.bind(origin)
// Eventually, destination will match origin, and stay in sync with any further changes to origin.
```

While bound, a watchable cannot be independently modified, and attempts to do so in `use` will throw.

## Batching

It's possible to listen for lists of changes, or even to receive updates on a reliable, but less-frequent basis. See the documentation for [batch](https://gladed.github.io/watchable/latest/io.gladed.watchable/kotlinx.coroutines.-coroutine-scope/batch.html), especially the `minPeriod` parameter.

```kotlin
val list = listOf(4, 5).toWatchableList()
batch(list) { println(it) } // Prints: [Initial(initial=[4, 5])]
list.use { add(6); add(7) } // Prints: [Add(index=2, added=6), Add(index=3, added=7)]
```

## Grouping

You can group several watchables into a `WatchableGroup` so that you receive changes for both:

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

`CoroutineScope` lifetime is respected. This means a `watch` or `bind` automatically stops operating when the related scope(s) complete. No additional cleanup code is required.

## Subscribing

You can [subscribe](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/subscribe.html) to changes on a watchable, returning an ordinary ReceiveChannel which receives lists of changes as they occur. However, it is usually more convenient to use `bind()` and `watch { ... }` as described above.

# Sample

See the [Sample Project](/sample) for some ideas on how this could be integrated into a project.

# Version History

See [HISTORY.md](HISTORY.md)
