package com.livefyre.akka.cdi

import javax.enterprise.inject.spi.BeanManager

import akka.actor.{ActorSystem, Extension, Props}

class CDIExtensionImpl extends Extension {
  var bm: BeanManager = _

  def initialize(implicit bm: BeanManager): CDIExtensionImpl = {
    this.bm = bm
    this
  }

  def props(actorBeanName: String): Props = {
    Props(classOf[ActorProducer], bm, actorBeanName)
  }
}

object CDIExtensionImpl {
  def apply(system: ActorSystem) (implicit bm: BeanManager): CDIExtensionImpl = CDIExtension().get(system).initialize
}