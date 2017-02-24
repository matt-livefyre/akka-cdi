package com.livefyre.akka.cdi

import javax.annotation.{PostConstruct, PreDestroy}
import javax.enterprise.inject.spi.BeanManager
import javax.inject.Inject

import akka.actor.{ActorRef, ActorSystem}
import com.livefyre.tools.service.BaseService

import scala.concurrent.ExecutionContextExecutor

class ActorSystemBean extends BaseService {

  @Inject
  implicit var bm: BeanManager = _

  val system = ActorSystem("akka-cdi")

  CDIExtensionImpl(system)

  val dispatcher: ExecutionContextExecutor = system.dispatcher

  var counter: ActorRef = _

  @PostConstruct
  def init(): Unit = {
    counter = system.actorOf(CDIExtensionImpl(system).props("countingActor"), "counter")
  }

  @PreDestroy
  def shutdown(): Unit = system.terminate()

}
