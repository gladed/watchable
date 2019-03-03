[ ![Download](https://api.bintray.com/packages/gladed/watchable/watchable/images/download.svg?version=0.5.3) ](https://bintray.com/gladed/watchable/watchable/0.5.3/link)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gladed_watchable&metric=alert_status)](https://sonarcloud.io/dashboard?id=gladed_watchable)
[![CircleCI](https://circleci.com/gh/gladed/watchable.svg?style=svg)](https://circleci.com/gh/gladed/watchable)
[![CodeCov](https://codecov.io/github/gladed/watchable/coverage.svg?branch=master)](https://codecov.io/github/gladed/watchable)
[![detekt](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://arturbosch.github.io/detekt/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.21-blue.svg)](https://kotlinlang.org/)
[![API Docs](https://img.shields.io/badge/API_Docs-0.5.3-purple.svg)](https://gladed.github.io/watchable/0.5.3/io.gladed.watchable/)

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
    compile 'io.gladed:watchable:0.5.3'
}
```

# Features

## Data Types

`WatchableList`, `WatchableSet`, `WatchableMap` allow access to wrapped List, Set, and Map data. `WatchableValue` wraps a single object value of any type.

These types are created on a CoroutineScope with `watchable___Of(...)`. For example: 

```kotlin
class MyClass : CoroutineScope {
    val list = watchableListOf(1, 2, 3)
    val map = watchableMapOf(4 to "four")
    val set = watchableSetOf(5.0, 6.0)
    val value = watchableValueOf(URI.create("https://github.com"))
```

Each data type can be accessed, modified, watched, and bound. 

## Accessing Data

You can obtain a read-only copy of the underlying data using `get()`. Note that `get()` may suspend for a short time while other coroutines complete modifications of the data.

For `WatchableSet`, `WatchableList`, and `WatchableMap` the returned data is guaranteed not to change, so you can safely iterate and access it normally.

```kotlin
val list = watchableListOf(1, 2, 3)
val listCopy: List<Int> = list.get()
for (value in listCopy) {
    println("$value")
} // Prints "1, 2, 3"
```

## Modifying Contents

A `MutableWatchable` can be modified with `use`, which takes a modifiable form of the underlying data as the receiver:

```kotlin
val list = watchableListOf(1, 2)
list.use { add(3) }
println("${list.get()}") // Prints "1, 2, 3" 
```  

`use` always suspends until any other coroutines are done modifying the object.

## Watching for Changes

You can watch any `Watchable` for changes from any `CoroutineScope`.

```kotlin
val set = watchableSetOf(1, 2)
set.watch { change -> 
    println("$change") // Prints "SetChange.Initial(1, 2)"
}
set.use { add(3) } // Prints "SetChange.Add(3)"
```

## Read-Only Watchable

You can use a `MutableWatchable`'s `.readOnly()` function to return a copy indicating it must not be changed externally. The copy may still be watched normally.

## Binding

A `bind` is just a `watch` that connects one watchable to another, so that the destination automatically gets and applies all changes.

```kotlin
val origin = listOf(4, 5).toWatchableList()
val destination = watchableListOf<Int>()
destination.bind(origin)
watch(destination) {
    println("Change: $it") // Prints "ListChange.Initial(4, 5)"
}
```

Objects cannot be modified while bound to another object.

## Object Lifetime

`CoroutineScope` lifetime is respected. This means a `watch` or `bind` automatically stops when either the watched object's scope completes, or if the initiating scope completes.

# Version History

See [HISTORY.md](HISTORY.md)
