package com.livefyre.akka.cdi

import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.inject.Produces
import javax.enterprise.inject.spi.{BeanManager, InjectionPoint}
import javax.inject.{Inject, Singleton}

import akka.actor.{Actor, ActorRef, ActorSystem}

import scala.collection.JavaConverters._

@Singleton
class ActorRefProducer {

  @Inject
  implicit var bm: BeanManager = _

  private val actorRefs = new ConcurrentHashMap[String, ActorRef]().asScala

  @Produces
  def getActorRef(ip: InjectionPoint, actorSystemProducer: ActorSystemProducer): ActorRef = {
    val actorQualifier = ip.getAnnotated.getAnnotation(classOf[UsingActor])
    val system = actorSystemProducer.getActorSystem(actorQualifier.actorSystem())
    getOrCreateActorRef(system, actorQualifier.typeOf(), actorQualifier.name())
  }

  def getOrCreateActorRef(actorSystem: ActorSystem, clazz: Class[_ <: Actor], name: String): ActorRef = {
    val key = generateKey(actorSystem, clazz, name)
    actorRefs.get(key) match {
      case Some(actorRef) => actorRef
      case None =>
        this.synchronized {
          actorRefs.get(key) match {
            case Some(actorRef) => actorRef
            case None =>
              val actorRef = actorSystem.actorOf(CDIExtensionImpl(actorSystem).props(clazz), name)
              actorRefs.put(key, actorRef)
              actorRef
          }
        }
    }
  }

  private def generateKey(actorSystem: ActorSystem, clazz: Class[_ <: Actor], name: String): String = {
    s"${actorSystem.name}_${clazz.getName}_$name"
  }

}
