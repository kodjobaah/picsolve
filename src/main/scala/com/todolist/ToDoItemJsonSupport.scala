package com.todolist

import com.todolist.domain.MyToDoItem
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.MetaMarshallers
import spray.json.DefaultJsonProtocol
import spray.json._

/**
 * Created by kodjobaah on 26/11/2015.
 */
object ToDoItemJsonSupport  extends DefaultJsonProtocol with SprayJsonSupport with MetaMarshallers{
  implicit val toDoItemJsonFormat = jsonFormat4(MyToDoItem)

}


