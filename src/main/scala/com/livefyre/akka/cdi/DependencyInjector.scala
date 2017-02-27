package com.livefyre.akka.cdi

import javax.enterprise.inject.spi.BeanManager

import akka.actor.{Actor, IndirectActorProducer}

import scala.collection.JavaConversions._

class DependencyInjector (bm: BeanManager, clazz: Class[_ <: Actor]) extends IndirectActorProducer {
  override def produce(): Actor = {
    bm.getBeans(clazz)
      .map(bean => bm.getReference(bean, classOf[Actor], bm.createCreationalContext(bean)))
      .head
      .asInstanceOf[Actor]
  }

  override def actorClass: Class[_ <: Actor] = {
    bm.getBeans(clazz).map(_.getBeanClass).head.asInstanceOf[Class[_ <: Actor]]
  }
}
