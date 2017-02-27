package com.livefyre.akka.cdi

import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.{CDI, InjectionPoint}

import akka.actor.ActorSystem

import scala.collection.JavaConverters._

class ActorSystemProducer {

  private val actorSystems = new ConcurrentHashMap[String, ActorSystem]().asScala

  @Produces
  def getNamedActorSystem(ip: InjectionPoint): ActorSystem = {
    getActorSystem(ip.getAnnotated.getAnnotation(classOf[UsingActorSystem]).name())
  }

  def getActorSystem(name: String): ActorSystem = {
    actorSystems.get(name) match {
      case Some(actorSystem) => actorSystem
      case None =>
        this.synchronized {
          actorSystems.get(name) match {
            case Some(actorSystem) => actorSystem
            case None =>
              val system = ActorSystem(name)
              actorSystems.put(name, system)
              system
          }
        }
    }
  }
}
