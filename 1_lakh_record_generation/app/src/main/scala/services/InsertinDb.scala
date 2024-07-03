package services

import com.typesafe.config.ConfigFactory
import model.Employee
import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection
import java.sql.{Connection, PreparedStatement, SQLException}
import scala.xml.XML

object InsertinDb {
  def main(args: Array[String]): Unit = {
    val logger: Logger = LoggerFactory.getLogger(getClass)
    val config = ConfigFactory.load().getConfig("database")
    val url = config.getString("Url")
    val username = config.getString("username")
    val password = config.getString("password")
    val xmlFile = "employees.xml"
    val xml = XML.loadFile(xmlFile)
    val employees = (xml \ "employee").map { employee =>
      val id = (employee \ "id").text.toInt
      val name = (employee \ "name").text
      val age = (employee \ "age").text.toInt
      val department = (employee \ "department").text
      val city = (employee \ "city").text
      val state = (employee \ "state").text
      Employee(id, name, age, department, city, state)
    }
    val connection: Connection = DatabaseConnection.getConnection
    val insert = "INSERT INTO Employees (ID, Name, Age, Department,City,State) VALUES (?,?,?,?,?,?)"
    val preparedStatement: PreparedStatement = connection.prepareStatement(insert)

    val startT = System.currentTimeMillis()
    employees.foreach { employee =>
      try {
        preparedStatement.setInt(1, employee.id)
        preparedStatement.setString(2, employee.name)
        preparedStatement.setInt(3, employee.age)
        preparedStatement.setString(4, employee.department)
        preparedStatement.setString(5, employee.city)
        preparedStatement.setString(6, employee.state)
        preparedStatement.executeUpdate()
      } catch {
        case e: SQLException if e.getErrorCode == 2627 =>
          logger.info(s"skip duplicate id ${employee.id}.")
        case e: SQLException =>
          logger.error(s"SQL ERROR OCCURED: ${e.getMessage}")
          e.printStackTrace()
      }
    }
    val endT = System.currentTimeMillis()
    val totalT = (endT - startT) / 1000.0
    logger.info(s" total inserted time: $totalT seconds")
    connection.close()
    logger.info(s"INSERTED ${employees.size} INTO THE DATABASE")
  }
}
