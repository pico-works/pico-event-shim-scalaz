# pico-atomic
[![CircleCI](https://circleci.com/gh/pico-works/pico-atomic/tree/develop.svg?style=svg)](https://circleci.com/gh/pico-works/pico-atomic/tree/develop)

Support library for atomic operations.

## Getting started

Add this to your SBT project:

```
resolvers += "dl-john-ky-releases" at "http://dl.john-ky.io/maven/releases"

libraryDependencies += "org.pico" %%  "pico-atomic" % "0.0.1-2"
```

## Atomic updates
A value can be updated atomically with a transformation function using the update method:

      import org.pico.atomic.syntax.std.atomicInteger._
      import java.util.concurrent.atomic.AtomicInteger
      val ref = new AtomicInteger(1)
      ref.update(_ + 1) must_== (1, 2)
      ref.get must_== 2

      import org.pico.atomic.syntax.std.atomicReference._
      import java.util.concurrent.atomic.AtomicReference
      val ref = new AtomicReference[Int](1)
      ref.update(_ + 1) must_== (1, 2)
      ref.get must_== 2

The update function will retry applying the transformation function until a successful
atomic update occurs.
