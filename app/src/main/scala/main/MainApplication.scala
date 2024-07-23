package main

import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection
import services.{CrudOperations, ExtractDatabase, GenerateEmployeeXml}

import java.sql.Connection
import scala.util.{Failure, Success, Try}

object MainApplication extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  val connection: Try[Connection] = Try {
    DatabaseConnection.getConnection
  }

  connection match {
    case Success(connection) =>
      logger.info("connection has been successfully established!")
      val generateEmployeeXml = new GenerateEmployeeXml
      generateEmployeeXml.insertDataIntoDataBase(connection)
      new CrudOperations(connection).menu()
      val outputFilePath ="extracted_employees.xml"
      ExtractDatabase.extractData(connection,outputFilePath)
    case Failure(exception) =>
      logger.error(s"connection disrupted! ${exception.getMessage}")
  }
}

