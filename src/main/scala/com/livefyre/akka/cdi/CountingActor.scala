package com.livefyre.akka.cdi

import javax.inject.Inject

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.livefyre.akka.cdi.TickActor.{TickGet, Tock}
import com.livefyre.tools.cdi.ConfigValue

import scala.concurrent.Await
import scala.concurrent.duration._

object CountingActor {
  case object Tick
  case object Get

  def props(tickActor: ActorRef): Props = Props(new CountingActor(tickActor))
}

class CountingActor @Inject() (@UsingActor(typeOf = classOf[TickActor], name = "tick") tick: ActorRef) extends Actor {
  import CountingActor._

//  @Inject
//  @UsingActor(typeOf = classOf[TickActor], name = "tick")
//  var tick: ActorRef = _

  @Inject
  @ConfigValue("akka_cdi.counter")
  var count: Int = _

  override def receive: Receive = {
    case Tick => tick ! Tock
    case Get =>
      implicit val timeout = Timeout(5.seconds)
      val future = tick ? TickGet
      val result = Await.result(future, timeout.duration).asInstanceOf[Int]
      sender ! result
  }
}
