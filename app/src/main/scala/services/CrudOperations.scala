package services

import model.Employee
import org.slf4j.{Logger, LoggerFactory}
import repository.DbQueryImp

import java.sql.Connection
import scala.annotation.tailrec

trait UserInput {
  def readLine(prompt: String): String
}

class ConsoleUserInput extends UserInput {
  override def readLine(prompt: String): String = {
    println(prompt)
    scala.io.StdIn.readLine()
  }
}

class CrudOperations(connection: Connection, userInput: UserInput) extends DbQueryImp {
  override val logger: Logger = LoggerFactory.getLogger(getClass)

  def menu(): Unit = {
    try {
      @tailrec
      def loop(): Unit = {
        logger.info("Choose an operation:")
        logger.info("1. Insert employee")
        logger.info("2. Update employee")
        logger.info("3. Delete employee")
        logger.info("4. Get employee by ID")
        logger.info("5. Get all employees")
        logger.info("6. Exit")
        logger.info("Enter your choice:")

        userInput.readLine("").toInt match {
          case 1 => insertEmployee()
          case 2 => updateById()
          case 3 => deleteById()
          case 4 => getById()
          case 5 => getAll // Retrieve and log all employees
          case 6 => return
          case _ => logger.info("Invalid choice. Please try again.")
        }
        loop()
      }

      loop()
    } catch {
      case e: Exception =>
        logger.error(s"An error occurred during CRUD operations: ${e.getMessage}")
    }
  }

  def insertEmployee(): Unit = {
    val id = userInput.readLine("Enter employee ID:").toInt
    val name = userInput.readLine("Enter employee name:")
    val age = userInput.readLine("Enter employee age:").toInt
    val department = userInput.readLine("Enter employee department:")
    val city = userInput.readLine("Enter employee city:")
    val state = userInput.readLine("Enter employee state:")
    val timestamp = userInput.readLine("inserted time:")

    val employee = Employee(id, name, age, department, city, state, timestamp)
    val result = insertEmployeeData(connection, employee)
    logger.info(result)
  }

  def updateById(): Unit = {
    val id = userInput.readLine("Enter employee ID:").toInt
    val name = userInput.readLine("Enter employee name:")
    val age = userInput.readLine("Enter employee age:").toInt
    val department = userInput.readLine("Enter employee department:")
    val city = userInput.readLine("Enter employee city:")
    val state = userInput.readLine("Enter employee state:")
    val timestamp = userInput.readLine("inserted time:")

    val employee = Employee(id, name, age, department, city, state, timestamp)
    val result = updateById(connection, id, employee)
    logger.info(result)
  }

  def deleteById(): Unit = {
    val id = userInput.readLine("Enter employee ID to delete:").toInt

    val result = deleteById(connection, id)
    logger.info(result)
  }

  def getById(): Unit = {
    val id = userInput.readLine("Enter employee ID to retrieve:").toInt

    val employee = getById(connection, id)
    employee match {
      case Some(emp) => logger.info(s"Employee details: $emp")
      case None => logger.info(s"No employee found with ID $id")
    }
  }

   def getAll: List[Employee] = {
    val employees = super.getAll(connection)
    employees.foreach(employee => logger.info(s"Employee details: $employee"))
    employees
  }
}
