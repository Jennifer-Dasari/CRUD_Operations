//package main
//
//import org.slf4j.{Logger, LoggerFactory}
//import repository.DatabaseConnection
//import services.{CrudOperations, ExtractDatabase, GenerateEmployeeXml}
//
//import java.sql.Connection
//import scala.util.{Failure, Success, Try}
//
//object MainApplication extends App {
//  val logger: Logger = LoggerFactory.getLogger(getClass)
//  val databaseConnection = new DatabaseConnection
//
//  val connection: Try[Connection] = Try {
//    databaseConnection.getConnection
//  }
//
//  connection match {
//    case Success(connection) =>
//      logger.info("connection has been successfully established!")
//
//      val generateEmployeeXml = new GenerateEmployeeXml
//      generateEmployeeXml.insertDataIntoDataBase(connection)
//
//      new CrudOperations(connection).menu()
//
//      val outputFilePath = "extracted_employees.xml"
//      val extractDatabase = new ExtractDatabase
//      extractDatabase.extractData(connection,outputFilePath)
//
////      databaseConnection.closeConnection(connection)
//    case Failure(exception) =>
//      logger.error(s"connection disrupted! ${exception.getMessage}")
//  }
//}


package main

import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection
import services.{ConsoleUserInput, CrudOperations, ExtractDatabase, GenerateEmployeeXml}

import java.sql.Connection
import scala.util.{Failure, Success, Try}

object MainApplication {
  def main(args: Array[String]): Unit = {
    val logger: Logger = LoggerFactory.getLogger(getClass)
    val userInput = new ConsoleUserInput
    //    val databaseConnection = new DatabaseConnection
    run(
      new DatabaseConnection,
      new GenerateEmployeeXml,
      (conn:Connection) =>new CrudOperations(conn,userInput),
      new ExtractDatabase( new DatabaseConnection,logger),
      logger
    )
  }
  def run(
           databaseConnection: DatabaseConnection,
           generateEmployeeXml: GenerateEmployeeXml,
           crudOperationsFactory: Connection => CrudOperations,
           extractDatabase: ExtractDatabase,
           logger: Logger
         ): Unit = {
    val connection: Try[Connection] = Try {
      databaseConnection.getConnection
    }
    connection match {
      case Success(conn) =>
        logger.info("connection has been successfully established!")
        generateEmployeeXml.insertDataIntoDataBase(conn)
        crudOperationsFactory(conn).menu()
        val outputFilePath = "extracted_employees.xml"
        extractDatabase.extractData(conn, outputFilePath)
        databaseConnection.closeConnection(conn)
      case Failure(exception) =>
        logger.error(s"connection disrupted! ${exception.getMessage}")
    }
  }
}

