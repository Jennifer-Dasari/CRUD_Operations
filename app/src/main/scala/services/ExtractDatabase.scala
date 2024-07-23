package services

import model.Employee
import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection

import java.io.FileWriter
import java.sql.Connection

object ExtractDatabase {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  def extractData(connection: Connection,filePath:String): Unit = {
    val query = "SELECT * FROM Employees"
    val statements = connection.createStatement()
    val resultSet = statements.executeQuery(query)
    //    val opFileName = "extracted_employees.xml"
    val writer = new FileWriter(filePath)
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
    writer.write("<employees>\n")
    try {
      while (resultSet.next()) {
        val id = resultSet.getInt("Id")
        val name = resultSet.getString("Name")
        val age = resultSet.getInt("Age")
        val department = resultSet.getString("Department")
        val city = resultSet.getString("City")
        val state = resultSet.getString("State")
        val timestamp = resultSet.getString("Timestamp")
        val employee = Employee(id, name, age, department, city, state, timestamp)

        writer.write(s"<employee>\n")
        writer.write(s"<id>${employee.id}</id>\n")
        writer.write(s"<name>$employee.name</name>\n")
        writer.write(s"<age>$employee.age</age>\n")
        writer.write(s"<department>$employee.department</department>\n")
        writer.write(s"<city>$employee.city</city>\n")
        writer.write(s"<state>$employee.state</state>\n")
        writer.write(s"<timestamp>${employee.timestamp}</timestamp>\n") // Added timestamp
        writer.write(s"</employee>\n")
      }
      writer.write("</employees>\n")
      //      writer.close()
    }
    catch {
      case e: Exception =>
        logger.error(s"SQL ERROR OCCURED: ${e.getMessage}")
    } finally {
      writer.close()
      statements.close()
      DatabaseConnection.closeConnection(connection)
    }
    logger.info(s"Data extracted and has been saved in $filePath!")

  }
}