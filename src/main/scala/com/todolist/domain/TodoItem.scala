package com.todolist.domain

/**
 * Created by kodjobaah on 26/11/2015.
 */
trait TodoItem {

  // Id, unique identifier for each item. This should be created by the server on creation
  def id: Long

  // Integer from 1 to 5 with the level of priority, where higher number means higher priority
  def priority: Int

  // Description of the task, it shouldn't be empty
  def description: String

  // Boolean to mark a task as it has been done
  def isDone: Boolean

}
