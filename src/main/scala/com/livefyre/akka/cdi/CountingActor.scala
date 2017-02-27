package com.livefyre.akka.cdi

import javax.enterprise.context.Dependent
import javax.inject.{Inject, Named}

import akka.actor.{Actor, Props}
import com.livefyre.tools.cdi.ConfigValue

object CountingActor {
  case object Tick
  case object Get

  def props(countingService: CountingService): Props = Props(new CountingActor(countingService))
}

@Named
class CountingActor @Inject() (countingService: CountingService) extends Actor {
  import CountingActor._

  @Inject
  @ConfigValue("akka_cdi.counter")
  var count: Int = _

  override def receive: Receive = {
    case Tick => count = countingService.increment(count)
    case Get  => sender ! count
  }
}
