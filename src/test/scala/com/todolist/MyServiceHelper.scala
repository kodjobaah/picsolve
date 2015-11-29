package com.todolist

import akka.actor.{ActorSystem, ActorRefFactory}
import com.todolist.domain.MyToDoItem
import org.specs2.mutable.Specification
import spray.http.{HttpCharsets, HttpEntity, MediaTypes, ContentTypes}
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import spray.json._
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

/**
 * Created by kodjobaah on 29/11/2015.
 */
class MyServiceHelper  extends Specification with Specs2RouteTest with HttpService with MyService {



  import com.todolist.ToDoItemJsonSupport._
  import spray.json._

  def createToDoItem(): MyToDoItem = {
    val r = scala.util.Random
     var item: MyToDoItem = MyToDoItem(priority=r.nextInt(5), description = "testing the value for update")

    implicit def sprayJsonMarshaller[T](implicit writer: RootJsonWriter[T], printer: JsonPrinter = PrettyPrinter) =
      Marshaller.delegate[T, String](ContentTypes.`application/json`) { value ⇒
        val json = writer.write(value)
        printer(json)
      }
    Post("/todolist",item) ~> myRoute ~> check {
    implicit def sprayJsonUnmarshaller[T: RootJsonReader] =
        Unmarshaller[T](MediaTypes.`application/json`) {
          case x: HttpEntity.NonEmpty ⇒
            val json = JsonParser(x.asString(defaultCharset = HttpCharsets.`UTF-8`))
            jsonReader[T].read(json)
        }
      responseAs[MyToDoItem]
    }
  }

  def createItemAndMarkAsDone(): MyToDoItem = {

      val item = createToDoItem()

      Post("/todolist/markdone/"+item.id) ~> myRoute ~> check {
      implicit def sprayJsonUnmarshaller[T: RootJsonReader] =
        Unmarshaller[T](MediaTypes.`application/json`) {
          case x: HttpEntity.NonEmpty ⇒
            val json = JsonParser(x.asString(defaultCharset = HttpCharsets.`UTF-8`))
            jsonReader[T].read(json)
        }
      responseAs[MyToDoItem]
    }
  }

  def actorRefFactory: ActorRefFactory = ActorSystem("spray-test-todolist")
}


object MyServiceHelper extends MyServiceHelper