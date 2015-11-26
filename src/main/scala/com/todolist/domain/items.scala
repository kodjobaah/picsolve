package com.todolist.domain

/**
 * Created by kodjobaah on 26/11/2015.
 */
case class MyToDoItem(id: Long = -1 , priority:Int, description: String, isDone: Boolean = false) extends TodoItem
