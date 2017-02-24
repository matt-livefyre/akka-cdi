package com.livefyre.akka.cdi

import akka.actor.{AbstractExtensionId, ActorSystem, ExtendedActorSystem}

object CDIExtension {
  def apply(): CDIExtension = new CDIExtension
}

class CDIExtension extends AbstractExtensionId[CDIExtensionImpl] {
  override def createExtension(system: ExtendedActorSystem): CDIExtensionImpl = new CDIExtensionImpl

  override def get(system: ActorSystem): CDIExtensionImpl = super.get(system)
}
