package com.todolist

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import scala.concurrent.duration._
import spray.can.Http
import akka.pattern.ask

/**
 * Created by kodjobaah on 26/11/2015.
 */
class Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "todolist-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8080)
}
