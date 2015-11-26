package com.todolist.service

import akka.actor.Actor
import com.todolist.domain.MyToDoItem


/**
 * Created by kodjobaah on 26/11/2015.
 */


case class CreateTodoItem(todoItem: MyToDoItem)

class ToDoItemsActor extends Actor with TodoActions {

  override def receive: Receive = {

    case CreateTodoItem(todoItem) =>
      sender ! createTodoItem(todoItem)

  }
}


trait TodoActions {

  def createTodoItem(todoitem: MyToDoItem): MyToDoItem = {

    println(todoitem)
    todoitem
  }
}