package com.livefyre.akka.cdi

import javax.enterprise.inject.spi.BeanManager

import akka.actor.{Actor, IndirectActorProducer}

class ActorProducer (bm: BeanManager, actorBeanName: String) extends IndirectActorProducer {
  override def produce(): Actor = {
    val bean = bm.getBeans(actorBeanName).iterator().next()
    val context = bm.createCreationalContext(bean)
    bm.getReference(bean, classOf[Actor], context).asInstanceOf[Actor]
  }

  override def actorClass: Class[_ <: Actor] = {
    val bean = bm.getBeans(actorBeanName).iterator().next()
    bean.getBeanClass.asInstanceOf[Class[_ <: Actor]]
  }
}
