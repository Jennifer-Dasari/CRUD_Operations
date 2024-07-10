package services

import model.Employee
import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection

import java.io.FileWriter
import java.sql.{Connection, SQLException}


object InsertinXml {
  def main(args: Array[String]): Unit = {
    val logger: Logger = LoggerFactory.getLogger(getClass)
    val connection: Connection = DatabaseConnection.getConnection

    val query = "SELECT * FROM Employees"
    val statements = connection.createStatement()
    val resultSet = statements.executeQuery(query)
    val opFileName = "extracted_employees.xml"
    val writer = new FileWriter(opFileName)
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
        val employee = Employee(id, name, age, department, city, state)

        writer.write(s"<employee>\n")
        writer.write(s"<id>$id</id>\n")
        writer.write(s"<name>$name</name>\n")
        writer.write(s"<age>$age</age>\n")
        writer.write(s"<department>$department</department>\n")
        writer.write(s"<city>$city</city>\n")
        writer.write(s"<state>$state</state>\n")
        writer.write(s"</employee>\n")
      }
      writer.write("</employees>\n")
      writer.close()
      logger.info(s"Data extracted and has been saved in $opFileName !")
    } catch {
      case e: SQLException =>
        logger.error(s"SQL ERROR OCCURED: ${e.getMessage}")
        e.printStackTrace()
    } finally {
      connection.close()
    }
  }
}