package services
import com.typesafe.config.ConfigFactory
import model.Employee
import repository.DatabaseConnection

import java.sql.{Connection, PreparedStatement, SQLException}
import scala.xml.XML

object InsertinDb {
  def main(args: Array[String]): Unit = {
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
//    Using.Manager { use =>
      val connection: Connection = DatabaseConnection.getConnection
      val insert = "INSERT INTO Employees (ID, Name, Age, Department,City,State) VALUES (?,?,?,?,?,?)"
      val preparedStatement: PreparedStatement = connection.prepareStatement(insert)
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
            println(s"skip duplicate id ${employee.id}.")
          case e: SQLException =>
            println(s"SQL ERROR OCCURED: ${e.getMessage}")
            e.printStackTrace()
        }
      }
    connection.close()
      println(s"INSERTED ${employees.size} INTO THE DATABASE")
//    } recover {
//      case e: Exception =>
//        println(s"Error occurred: ${e.getMessage}")
//        e.printStackTrace()
//    }
  }
}








