package services

import model.Employee
import org.slf4j.LoggerFactory
import repository.DbQueryImp

import java.sql.Connection
import scala.io.StdIn

class CrudOperations(connection: Connection) extends DbQueryImp {
  private val logger = LoggerFactory.getLogger(getClass)

  def menu(): Unit = {
    try {
      def loop(): Unit = {
        logger.info("Choose an operation:")
        logger.info("1. Insert employee")
        logger.info("2. Update employee")
        logger.info("3. Delete employee")
        logger.info("4. Get employee by ID")
        logger.info("5. Get all employees")
        logger.info("6. Exit")
        logger.info("Enter your choice:")

        StdIn.readLine().toInt match {
          case 1 => insertEmployee()
          case 2 => updateById()
          case 3 => deleteById()
          case 4 => getById()
          case 5 => getAll()
          case 6 => return
          case _ => logger.info("Invalid choice. Please try again.")
        }
        loop()
      }

      loop()
    } catch {
      case e: Exception =>
        logger.error(s"An error occurred during CRUD operations: ${e.getMessage}")
//        e.printStackTrace()
    }
  }

  def insertEmployee(): Unit = {
    logger.info("Enter employee ID:")
    val id = StdIn.readLine().toInt
    logger.info("Enter employee name:")
    val name = StdIn.readLine()
    logger.info("Enter employee age:")
    val age = StdIn.readLine().toInt
    logger.info("Enter employee department:")
    val department = StdIn.readLine()
    logger.info("Enter employee city:")
    val city = StdIn.readLine()
    logger.info("Enter employee state:")
    val state = StdIn.readLine()
    logger.info("inserted time:")
    val timestamp = StdIn.readLine()

    val employee = Employee(id, name, age, department, city, state, timestamp)
    val result = insertEmployeeData(connection, employee)
    logger.info(result)
  }

  def updateById(): Unit = {
    logger.info("Enter employee ID to update:")
    val id = StdIn.readLine().toInt
    logger.info("Enter new employee name:")
    val name = StdIn.readLine()
    logger.info("Enter new employee age:")
    val age = StdIn.readLine().toInt
    logger.info("Enter new employee department:")
    val department = StdIn.readLine()
    logger.info("Enter new employee city:")
    val city = StdIn.readLine()
    logger.info("Enter new employee state:")
    val state = StdIn.readLine()
    logger.info("updated time is:")
    val timestamp = StdIn.readLine()

    val employee = Employee(id, name, age, department, city, state, timestamp)
    val result = updateById(connection, id, employee)
    logger.info(result)
  }

  def deleteById(): Unit = {
    logger.info("Enter employee ID to delete:")
    val id = StdIn.readLine().toInt
    val result = deleteById(connection, id)
    logger.info(result)
  }

  def getById(): Unit = {
    logger.info("Enter employee ID to retrieve:")
    val id = StdIn.readLine().toInt
    val employee = getById(connection, id)
    employee match {
      case Some(emp) => logger.info(s"Employee details: $emp")
      case None => logger.info(s"No employee found with ID $id")
    }
  }

  def getAll(): Unit = {
    val employees = getAll(connection)
    employees.foreach(emp => logger.info(s"Employee details: $emp"))
  }
}
