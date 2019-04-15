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
coroutineScope {
    val set = watchableSetOf(1, 2)
    watch(set) { println("Got $it") }
    set.add(3)
}

// Output:
// Got Initial(set=[1, 2])
// Got Add(add=[3])
```

## Why?

Sometimes, you want a data structure that can be shared between objects and listen for changes to that data. But it's hard to remember and unregister all of those listeners. If you don't, you'll leak memory.

`Watchable` solves this by allowing operations (like `watch` and`bind`) which are limited to the lifetime of the `CoroutineScope` where they are created. When the scope completes, its watchable operations also stop and cleaned up automatically.

This can be useful in [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) designs, in which the data model lives at the center, and everyone depends on it. If the data model is defined in terms of `Watchable` objects, then other components (having their own lifecycles) can simply watch for changes and react accordingly, without coupling directly to each other.

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

## Reading and Writing Data

You can use WatchableMap, WatchableList, and WatchableSet anywhere you would use a normal Kotlin Map, List, or Set. WatchableValue wraps a single object. Their contents may be changed safely at any time:

```kotlin
val map = watchableMapOf(1 to "1")
println(map) // Prints {1=1}
map.put(2, "2") // Suspends if concurrent modification attempted
println(map) // Prints {1=1, 2=2}
```

To do multiple operations in a protected way, [`use`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/use.html) the object:

```kotlin
val list = watchableListOf(1, 2, 3)
// Remove last element safely
println(list.use { removeAt(list.size - 1) }) // Removes last element, prints 3
```

Note: some extension functions on List are unreliable if the data is modified from separate threads, since these functions assume List cannot change during execution. For example, [List.last()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/last.html) and [List.getOrElse()](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/get-or-else.html) access the size and then an element in separate steps.

## Watching for Changes

You can watch any `Watchable` for changes from any `CoroutineScope` using [`watch`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable/watch.html):

```kotlin
val set = watchableSetOf(1, 2)
watch(set) { println(it) }
set += 3
set -= listOf(3, 2)

// Prints:
// Initial(set=[1, 2])
// Add(add=[3])
// Remove(remove=[3, 2])
```

The `watch` operation lasts as long as the current coroutine scope. 

## Binding

A [`bind`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/bind.html) is just a `watch` that connects one watchable of the same type to another, so that the destination automatically receives all changes from an origin.

```kotlin
val from = listOf(4, 5).toWatchableList()
val into = watchableListOf<Int>()
bind(into, from) // "this" is the current coroutine scope
// ...time passes...
println(from == into) // true
```

While bound, a watchable cannot be independently modified, and attempts to do so in `use` will throw.

Complex binds are possible in which changes are received and may be arbitrarily mapped into changes on a bound item:

```kotlin
val from = listOf(4, 5).toWatchableList()
val into = watchableValueOf(0)
into.bind(this, from) {
    // Update size whenever "from" changes in any way
    value = from.size
}
// ...time passes...
println(into) // 2
```


## Batching

It's possible to listen for lists of changes, collected and delivered in-order periodically. See the documentation for [batch](https://gladed.github.io/watchable/latest/io.gladed.watchable/kotlinx.coroutines.-coroutine-scope/batch.html), especially the `minPeriod` parameter.

```kotlin
val list = listOf(4, 5).toWatchableList()
batch(list, 50) { println(it) }
list.use { add(6); add(7) }

// After time passes, prints:
// [Initial(list=[4, 5]), Add(index=2, added=6), Add(index=3, added=7)]
```

## Read-Only Watchables

You can use any [`MutableWatchable`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/)'s [`readOnly()`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-mutable-watchable/read-only.html) function to return a `Watchable` which cannot be changed externally. The copy may still be watched normally.

## Grouping

You can [`group`](https://gladed.github.io/watchable/latest/io.gladed.watchable/group.html) several watchables into a [`WatchableGroup`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watchable-group/) so that you receive changes for both:

```kotlin
val set = setOf("a").toWatchableSet()
val list = listOf(4).toWatchableList()
watch(group(set, list)) { println(it) }
// Prints:
// GroupChange(watchable=[a], change=Initial(set=[a]))
// GroupChange(watchable=[4], change=Initial(list=[4]))

list += 6
set += "b"
// Prints:
// GroupChange(watchable=[4, 6], change=Insert(index=1, insert=[6]))
// GroupChange(watchable=[a, b], change=Add(add=[b]))""")
```

## Simple Watches

You may not really care about the details of a change, or just want to respond to simple adds and removes of values. A simplified syntax allows you to see handle incremental changes as the receiver of your lambda:

```kotlin
val map = watchableMapOf(1 to "2")
simple(map) { println("at $key remove $remove add $add") }
map.put(1, "3")

// Prints:
// at 1 remove null add 2
// at 1 remove 2 add 3
```

## Object Lifetime

The initiating `CoroutineScope` lifetime is respected. Operations such as `watch` or `bind` will automatically stop when the initiating scope completes. No additional cleanup code is required.

Some operations return return a `WatchHandle` which can be used to stop the operation with more granular control. For example, calling `close` on the [`WatchHandle`](https://gladed.github.io/watchable/latest/io.gladed.watchable/-watch-handle/) returned by `watch` operation allows any pending changes to be processed to completion.

```kotlin
val list = watchableListOf(1)
val handle = watch(list) { println(it) }
list.add(2)
handle.close() // No further notifications after this point
list.add(3)

// Prints:
// Initial(list=[1])
// Insert(index=1, insert=[2])
```

# Sample

See the [Sample Project](/sample) for some ideas on how this could be integrated into a project.

# Version History

See [HISTORY.md](HISTORY.md)
