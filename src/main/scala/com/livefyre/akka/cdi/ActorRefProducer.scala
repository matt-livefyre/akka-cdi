package com.livefyre.akka.cdi

import javax.enterprise.inject.{Default, Produces}
import javax.enterprise.inject.spi.{BeanManager, CDI, InjectionPoint}

import akka.actor.ActorRef

class ActorRefProducer {

  private implicit val bm = CDI.current().getBeanManager

  @Produces
  def getActorRef(ip: InjectionPoint, actorSystemProducer: ActorSystemProducer): ActorRef = {
    val actorQualifier = ip.getAnnotated.getAnnotation(classOf[UsingActor])
    val system = actorSystemProducer.getActorSystem(actorQualifier.actorSystem())
    system.actorOf(CDIExtensionImpl(system).props(actorQualifier.typeOf()))
  }

}

