package com.todolist.domain

import slick.driver.MySQLDriver.api._

/**
 * Created by kodjobaah on 26/11/2015.
 */
case class MyToDoItem(id: Long = -1 , priority:Int, description: String, isDone: Boolean = false) extends TodoItem {
   require(priority >= 1 && priority <= 5)
}



class Items(tag: Tag) extends Table[MyToDoItem](tag,"ToDoItem") {
  def id = column[Long] ("id",O.PrimaryKey, O.AutoInc)
  def priority = column[Int]("priority")
  def description = column[String]("descritpion")
  def isDone = column[Boolean]("isDone")
  def * = (id,priority,description,isDone) <> (MyToDoItem.tupled, MyToDoItem.unapply)
}



