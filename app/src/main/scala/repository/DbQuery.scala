package repository

import model.Employee

import java.sql.Connection

trait DbQuery {
  def createTable(connection: Connection): Unit

  def insertEmployeeData(connection: Connection, employee: Employee): Unit

  def updateEmployeeData(connection: Connection, employee: Employee): Unit

  def deleteEmployeeData(connection: Connection, id: Int): Unit

  def viewEmployeeData(connection: Connection, id: Int): Unit
}