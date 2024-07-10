package repository

import model.Employee

import java.sql.Connection

trait DbQuery {
  def createTable(connection: Connection): Int

  def insertEmployeeData(connection: Connection, employee: Employee): String

  def updateById(connection: Connection, id: Int, employee: Employee): String

  def deleteById(connection: Connection, id: Int): String

  def getById(connection: Connection, id: Int): Option[Employee]

  def getAll(connection: Connection): List[Employee]
}