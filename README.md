[ ![Download](https://api.bintray.com/packages/gladed/watchable/watchable/images/download.svg?version=0.5.0) ](https://bintray.com/gladed/watchable/watchable/0.5.0/link)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=gladed_watchable&metric=alert_status)](https://sonarcloud.io/dashboard?id=gladed_watchable)
[![CircleCI](https://circleci.com/gh/gladed/watchable.svg?style=svg)](https://circleci.com/gh/gladed/watchable)
[![CodeCov](https://codecov.io/github/gladed/watchable/coverage.svg?branch=master)](https://codecov.io/github/gladed/watchable)
[![detekt](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://arturbosch.github.io/detekt/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.3.21-blue.svg)](https://kotlinlang.org/)

# Watchable

This library provides listenable data structures using [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html).

This is especially useful in [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html). The data model lives at the center, and everyone points in. If the data model is defined as Watchable objects, then other components can simply listen for changes and react accordingly, without coupling directly to each other.  

# Usage

Add to `build.gradle`:

```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'io.gladed:watchable:0.5.0'
}
```

Then, you can create or watch from any object that implements `CoroutineScope`:

```kotlin
set = watchableSetOf(1, 2)
watch(set) {
    println("Got $it")
}
set.use { add(3) }
// Output:
//   Got Initial(initial=[1, 2])
//   Got Add(added=3)
```

# Features
* Watchable objects can be watched, and bound from different whatever scope is convenient.
* objects can be "bound" to each other, so that a change in one object causes a corresponding change to another one.
* Modifications are synchronized and can safely be applied from any thread.
* When the surrounding coroutine scope completes, everything is cleaned up automatically.
* Supports mutable and read-only types like List, Set, Map, and any wrapped object (Value).
* OK performance compared to synchronized collections.

# Version History

See [HISTORY.md](HISTORY.md)
