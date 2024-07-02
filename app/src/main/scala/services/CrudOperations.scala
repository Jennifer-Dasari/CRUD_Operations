package services

import main.MainApplication.logger
import model.Employee
import repository.{DatabaseConnection, DbQueryImp}

import java.sql.Connection
import scala.io.StdIn

class CrudOperations(connection: Connection) extends DbQueryImp {
  createTable(connection)

  def menu(): Unit = {
    try {
      logger.info("choose an operation")
      logger.info("1.insert employee")
      logger.info("2.update employee")
      logger.info("3.delete employee")
      logger.info("4.view employee")
      logger.info("5. exit")
      logger.info("ENTER YOUR CHOICE:")
      val choice = StdIn.readInt()
      choice match {
        case 1 =>
          val employee = readEmployeeFromConsole()
          insertEmployeeData(connection, employee)
          menu()
        case 2 =>
          val employee = readEmployeeFromConsole()
          updateEmployeeData(connection, employee)
          menu()
        case 3 =>
          logger.info("Enter the ID of the employee to delete: ")
          val id = StdIn.readInt()
          deleteEmployeeData(connection, id)
          menu()
        case 4 =>
          logger.info("Enter the ID of the employee to view the data: ")
          val id = StdIn.readInt()
          viewEmployeeData(connection, id)
          menu()
        case 5 =>
          logger.info("EXITING....")
        case _ =>
          logger.warn("INVALID CHOICE PLEASE GIVE VALID CHOICE: ")
          menu()
      }

    }
    catch {
      case e: Exception => logger.error("error at execution :", e)
    }
    finally {
      DatabaseConnection.closeConnection(connection)
    }
  }

  def readEmployeeFromConsole(): Employee = {
    logger.info("enter the details:")
    logger.info("ID:")
    val id = StdIn.readInt()
    logger.info("NAME:")
    val name = StdIn.readLine()
    logger.info("AGE:")
    val age = StdIn.readInt()
    logger.info("DEPARTMENT:")
    val department = StdIn.readLine()
    logger.info("SALARY:")
    val salary = StdIn.readInt()
    logger.info("HOUSE NUMBER:")
    val hno = StdIn.readInt()
    logger.info("STREET:")
    val street = StdIn.readLine()
    logger.info("CITY:")
    val city = StdIn.readLine()
    logger.info("STATE:")
    val state = StdIn.readLine()
    logger.info("ZIPCODE:")
    val zipcode = StdIn.readInt()
    logger.info("Employee details inserted successfully")
    Employee(id, name, age, department, salary, hno, street, city, state, zipcode)
  }
}
