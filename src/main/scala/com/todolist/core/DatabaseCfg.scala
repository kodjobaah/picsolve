package com.todolist.core

import com.todolist.domain.{Items}

import slick.jdbc.meta.MTable
import slick.lifted.TableQuery
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


/**
 * Created by kodjobaah on 26/11/2015.
 */
object DatabaseCfg {


  val db = Database.forConfig("mysql1")

  // the base query for the Users table
  val items = TableQuery[Items]
  def init() = {


    val tables = Await.result(db.run(MTable.getTables), 1.seconds).toList

    if (tables.size < 1) {

      Await.result(db.run(DBIO.seq(
        items.schema.create
      )), Duration.Inf)

    }
  }
}



