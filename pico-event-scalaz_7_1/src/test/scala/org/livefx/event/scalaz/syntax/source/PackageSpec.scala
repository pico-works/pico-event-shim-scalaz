package org.pico.event.scalaz.syntax.source

import org.pico.disposal.Disposer
import org.pico.disposal.std.autoCloseable._
import org.pico.event.Bus
import org.pico.event.syntax.source._
import org.specs2.mutable.Specification

import scalaz.{-\/, \/, \/-}

class PackageSpec extends Specification {
  "Bus" should {
    "should be able to divert left of disjunction into sink" ! {
      val inBus = Bus[String \/ Int]
      val ltBus = Bus[String]
      val rtBus = Bus[Int]

      val disposer = new Disposer

      disposer += inBus.divertLeft(ltBus).into(rtBus)

      val ltValue = ltBus.foldRight(List.empty[String])(_ :: _)
      val rtValue = rtBus.foldRight(List.empty[Int])(_ :: _)

      inBus.publish(-\/("A"))
      inBus.publish(\/-(1))
      inBus.publish(-\/("B"))
      inBus.publish(\/-(2))

      ltValue.value must_== List("B", "A")
      rtValue.value must_== List(2, 1)
    }

    "should be able to divert right of disjunction into sink" ! {
      val inBus = Bus[String \/ Int]
      val ltBus = Bus[String]
      val rtBus = Bus[Int]

      val disposer = new Disposer

      disposer += inBus.divertRight(rtBus).into(ltBus)

      val ltValue = ltBus.foldRight(List.empty[String])(_ :: _)
      val rtValue = rtBus.foldRight(List.empty[Int])(_ :: _)

      inBus.publish(-\/("A"))
      inBus.publish(\/-(1))
      inBus.publish(-\/("B"))
      inBus.publish(\/-(2))

      ltValue.value must_== List("B", "A")
      rtValue.value must_== List(2, 1)
    }
  }
}
