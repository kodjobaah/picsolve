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

class ToDoItemsActor extends Actor with TodoActions {

  override def receive: Receive = {

    case CreateTodoItem(todoItem) =>
      sender ! createTodoItem(todoItem)

    case GetToDoItems =>
      sender ! getToDoItems()
  }
}

import scala.concurrent.duration._
import slick.driver.MySQLDriver.api._
import DatabaseCfg._

trait TodoActions {

  def getToDoItems(): List[MyToDoItem] = {

    var result:List[MyToDoItem] = List()
    var res = for {
      it <- items
    } yield (it)

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