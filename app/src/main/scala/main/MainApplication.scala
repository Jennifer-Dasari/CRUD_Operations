package main

import org.slf4j.{Logger, LoggerFactory}
import repository.DatabaseConnection
import services.CrudOperations

import java.sql.Connection

object MainApplication extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  try {
    val connection: Connection = DatabaseConnection.getConnection
    logger.info("connection has been successfully established!")
    new CrudOperations(connection).menu()
  } catch {
    case e: Exception =>
      logger.error(s"connection disrupted! ${e.getMessage}")
  }
}
