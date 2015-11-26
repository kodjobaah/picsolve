package com.todolist

import akka.actor.{ActorSystem, ActorRefFactory}
import com.todolist.domain.MyToDoItem
import org.specs2.mutable.Specification
import spray.http.{HttpCharsets, HttpEntity, MediaTypes, ContentTypes}
import spray.http.StatusCodes._
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

/**
 * Created by kodjobaah on 26/11/2015.
 */
class MyServiceSpec extends Specification with Specs2RouteTest with HttpService with MyService {


  "when this service is called" should {
    "save an item when /todolist is supplied with appropriate json object" in {

      import com.todolist.ToDoItemJsonSupport._
      import spray.json._
      implicit def sprayJsonMarshaller[T](implicit writer: RootJsonWriter[T], printer: JsonPrinter = PrettyPrinter) =
        Marshaller.delegate[T, String](ContentTypes.`application/json`) { value ⇒
          val json = writer.write(value)
          printer(json)
        }

      val todoList = MyToDoItem(priority=1, description = "testing the value")
      val myTestId = todoList.id
      Post("/todolist", todoList) ~> myRoute ~> check {


        implicit def sprayJsonUnmarshaller[T: RootJsonReader] =
          Unmarshaller[T](MediaTypes.`application/json`) {
          case x: HttpEntity.NonEmpty ⇒
            val json = JsonParser(x.asString(defaultCharset = HttpCharsets.`UTF-8`))
            jsonReader[T].read(json)
        }

        val todo = responseAs[MyToDoItem]
        todo.id must not equalTo(myTestId)
        status must be(Created)
      }

    }
  }

  def actorRefFactory: ActorRefFactory = ActorSystem("spray-test-todolist")
}
