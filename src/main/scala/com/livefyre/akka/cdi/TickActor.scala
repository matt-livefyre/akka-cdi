package com.livefyre.akka.cdi

import javax.inject.Inject

import akka.actor.{Actor, Props}
import com.livefyre.tools.cdi.ConfigValue

object TickActor {
  case object Tock
  case object TickGet

  def props(countingService: CountingService): Props = Props(new TickActor(countingService))
}

class TickActor @Inject() (countingService: CountingService) extends Actor {
  import TickActor._

  @Inject
  @ConfigValue("akka_cdi.counter")
  var count: Int = _

  override def receive: Receive = {
    case Tock => count = countingService.increment(count)
    case TickGet  => sender ! count
  }
}
