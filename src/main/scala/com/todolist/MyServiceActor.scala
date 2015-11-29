package com.todolist
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import com.todolist.domain.MyToDoItem
import com.todolist.service._
import spray.http.StatusCodes
import spray.routing.HttpService
import akka.pattern.ask
import scala.concurrent.ExecutionContext


/**
 * Created by kodjobaah on 26/11/2015.
 */
class MyServiceActor  extends Actor with MyService {



// the HttpService trait defines only one abstract member, which
// connects the services environment to the enclosing actor or test
def actorRefFactory = context
// this actor only runs our route, but you could add
// other things here, like request stream processing
// or timeout handling
def receive = runRoute(myRoute)
}



// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  val toDoListActorSystem = ActorSystem("on-todo-list")

  val todoList = toDoListActorSystem.actorOf(Props[ToDoItemsActor], "todolist-actor")

  import ExecutionContext.Implicits.global
  import spray.util._
  import com.todolist.ToDoItemJsonSupport._

  implicit val timeout = new Timeout(2, TimeUnit.SECONDS)
  val myRoute =

    path("todolist") {
      post {
        respondWithStatus(StatusCodes.Created) {
          handleWith { myToDoItem: MyToDoItem =>
            (todoList ? CreateTodoItem(myToDoItem)).mapTo[MyToDoItem]
          }
        }
      }
    } ~
      path("todolist" / "list") {
        get {
          complete {
            (todoList ? GetToDoItems).mapTo[List[MyToDoItem]]
          }
        }
      } ~
      path("todolist" / "markdone" / LongNumber) { itemId =>
        post {
          respondWithStatus(StatusCodes.Accepted) {
            complete {
              val markDone = MarkDone(itemId, true)
              (todoList ? markDone).mapTo[MyToDoItem]
            }
          }
        }
      } ~
      path("todolist" / "marknotdone" / LongNumber) { itemId =>
        post {

          respondWithStatus(StatusCodes.Accepted) {
            complete {
              val markDone = MarkDone(itemId, false)
              (todoList ? markDone).mapTo[MyToDoItem]
            }
          }
        }
      } ~
      path("todolist" / "delete" / LongNumber) { itemId =>
        post {

          val deleteItem = DeleteItem(itemId)
          todoList ! deleteItem
          respondWithStatus(StatusCodes.Accepted) {
            complete("")
          }
        }
      }~
      path("todolist" / "status" / Segment / "priority" / Segment) { (status: String,priority: String)  =>
        get {

          try {

            val st = status.toBoolean
            val prt = priority.toInt
            validate(prt <= 5 && prt >= 1, s"Priority has to be greater than 1 and less than 5") {
              val filteredList = FilteredList(st, prt)
              complete {
                (todoList ? filteredList).mapTo[List[MyToDoItem]]
              }
            }

          } catch {
            case e: Exception => {
              respondWithStatus(StatusCodes.BadRequest) {
                complete("status is either true or false and priority is a number between 1 and 5")
              }

            }

          }
        }
      }
}