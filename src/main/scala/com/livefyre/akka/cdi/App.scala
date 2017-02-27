package com.livefyre.akka.cdi

import javax.annotation.{ManagedBean, PostConstruct, PreDestroy}
import javax.inject.{Inject, Singleton}
import javax.ws.rs.core.Response
import javax.ws.rs.{GET, POST, Path}

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.livefyre.akka.cdi.CountingActor.{Get, Tick}
import com.livefyre.tools.cdi.ConfigValue
import com.livefyre.tools.jersey.WebServer
import com.livefyre.tools.service.BaseService

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
@ManagedBean
@Path("/")
class App extends BaseService {

  @Inject
  @UsingActor(typeOf = classOf[CountingActor])
  var counter: ActorRef = _

  @Inject
  @ConfigValue("akka_cdi.port")
  var port: Int = _

  private lazy val web = new WebServer(port, cache = true)

  @PostConstruct
  def start(): Unit = {
    web.start()
  }

  @PreDestroy
  def stop(): Unit = {
    web.stop()
  }

  @POST
  @Path("/tick")
  def postTick: Response = {
    counter ! Tick
    Response.ok("Tick!").build()
  }

  @GET
  @Path("/tick")
  def getTick: Response = {
    implicit val timeout = Timeout(5.seconds)
    val future = counter ? Get
    val result = Await.result(future, timeout.duration).asInstanceOf[Int]
    Response.ok(s"Ticks -> $result").build()
  }
}
