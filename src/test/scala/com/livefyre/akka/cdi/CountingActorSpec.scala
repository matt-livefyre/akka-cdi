package com.livefyre.akka.cdi

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.livefyre.akka.cdi.CountingActor.{Get, Tick}
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

@RunWith(classOf[JUnitRunner])
class CountingActorSpec extends TestKit(ActorSystem("CountingActorSpec"))
  with ImplicitSender with FlatSpecLike with Matchers with MockFactory with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  behavior of "CountingActor"

  it should "tickticktick" in {
    val countingSvc = mock[CountingService]
    (countingSvc.increment _).expects(*).onCall({arg: Int => arg + 1}).repeated(3).times
    val actor = system.actorOf(CountingActor.props(countingSvc))
    actor ! Tick
    actor ! Tick
    actor ! Tick
    actor ! Get
    expectMsg(3)
  }
}
