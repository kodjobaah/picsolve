package com.todolist

import akka.actor.{ActorSystem, ActorRefFactory}
import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

/**
 * Created by kodjobaah on 26/11/2015.
 */
class MyServiceSpec extends Specification with Specs2RouteTest with HttpService with MyService {


  "when this service is called" should {
    "save an item when /todolist is supplied with appropriate json object" in {

      Post("/todolist", "hey") ~> myRoute ~> check {
        status must be(Created)
      }

    }
  }

  def actorRefFactory: ActorRefFactory = ActorSystem("spray-test-todolist")
}
