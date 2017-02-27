package com.livefyre.akka.cdi

import javax.enterprise.inject.spi.BeanManager

import akka.actor.{Actor, IndirectActorProducer}

class DependencyInjector (bm: BeanManager, clazz: Class[_ <: Actor]) extends IndirectActorProducer {
  override def produce(): Actor = {
    val bean = bm.getBeans(clazz).iterator().next()
    val context = bm.createCreationalContext(bean)
    bm.getReference(bean, classOf[Actor], context).asInstanceOf[Actor]
  }

  override def actorClass: Class[_ <: Actor] = {
    val bean = bm.getBeans(clazz).iterator().next()
    bean.getBeanClass.asInstanceOf[Class[_ <: Actor]]
  }
}
