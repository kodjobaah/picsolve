package com.todolist.service

import akka.actor.Actor
import com.todolist.core.DatabaseCfg
import com.todolist.domain.MyToDoItem

import scala.concurrent.Await


/**
 * Created by kodjobaah on 26/11/2015.
 */


case class CreateTodoItem(todoItem: MyToDoItem)
case class GetToDoItems()
case class MarkDone(id: Long, state: Boolean)
case class DeleteItem(id: Long)
case class FilteredList(status: Boolean, priority: Int)

class ToDoItemsActor extends Actor with TodoActions {

  override def receive: Receive = {

    case CreateTodoItem(todoItem) =>
      sender ! createTodoItem(todoItem)

    case GetToDoItems =>
      sender ! getToDoItems()

    case MarkDone(itemId,state) =>
      sender ! markItemDone(itemId,state)

    case DeleteItem(itemId) =>
       deleteItem(itemId)

    case FilteredList(status,priority) =>
       sender ! filterItems(status,priority)
  }
}

import scala.concurrent.duration._
import slick.driver.MySQLDriver.api._
import DatabaseCfg._

trait TodoActions {

  def filterItems(status: Boolean, priority:Int ): List[MyToDoItem] = {

    var res = for {
      it <- items if (it.isDone === status && it.priority === priority)
    } yield (it)

    var resp = db.run(res.result)
    var out = Await.result(resp,3 seconds)
    out.toList
  }

  def deleteById(itemId: Rep[Long]) = {
    items.filter(_.id === itemId)
  }

  val deleteByIdCompiled = Compiled(deleteById _)

  def deleteItem(itemId: Long) = {
    val action = deleteByIdCompiled(itemId).delete
    db.run(action)
    itemId
  }
  def findItemById(itemId: Rep[Long]) = {

     for {
        item <- items if item.id === itemId
     } yield item
  }

  def markItemAsDone(itemId: Rep[Long]) = {

    for {
      item <- items if item.id === itemId
    } yield item.isDone


  }
  val findItemByIdCompiled = Compiled(findItemById _)

  val markItemAsDoneCompiled = Compiled(markItemAsDone _)

  def markItemDone(itemId: Long, state: Boolean): MyToDoItem = {

    val markDone = markItemAsDone(itemId).update(state)

    var resp = db.run(markDone)
    var out = Await.result(resp,3 seconds)
    val itemToUpdate = findItemByIdCompiled(itemId).result
    val outItem = Await.result(db.run(itemToUpdate), 3 seconds).asInstanceOf[Vector[MyToDoItem]].head.asInstanceOf[MyToDoItem]
    outItem

  }
  def getToDoItems(): List[MyToDoItem] = {

    import scala.concurrent.ExecutionContext.Implicits.global

    var res = for {
      it <- items
    } yield (it)

    /*

import slick.backend.DatabasePublisher
val a = res.result
val p: DatabasePublisher[MyToDoItem] = db.stream(a)

var out:List[MyToDoItem] = List()

p.foreach { s =>
  println("inside:"+s)
  out = out :+ s
}
*/

    var resp = db.run(res.result)
    var out = Await.result(resp,3 seconds)
    out.toList

  }

  def createTodoItem(todoitem: MyToDoItem): MyToDoItem = {

    val res = db.run(
      (items returning items.map(_.id) into ((item,id) => item.copy(id=id))) += todoitem
    )
    val itemWithId = Await.result(res,3 seconds).asInstanceOf[MyToDoItem]
    itemWithId
  }
}