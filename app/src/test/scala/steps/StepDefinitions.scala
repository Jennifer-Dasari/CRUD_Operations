package steps

import io.cucumber.scala.{EN, ScalaDsl}
import org.mockito.ArgumentMatchers.{any, eq => eqMatcher}
import org.mockito.Mockito.{mock, times, verify, when}
import org.scalatest.Matchers
import org.slf4j.Logger
import repository.DatabaseConnection
import services.{CrudOperations, ExtractDatabase, GenerateEmployeeXml}

import java.sql.Connection
import scala.util.{Failure, Success, Try}

class StepDefinitions extends ScalaDsl with EN with Matchers {
  val logger: Logger = mock(classOf[Logger])
  val mockConnection: Connection = mock(classOf[Connection])
  val databaseConnection: DatabaseConnection = mock(classOf[DatabaseConnection])
  val generateEmployeeXml: GenerateEmployeeXml = mock(classOf[GenerateEmployeeXml])
  val crudOperations: CrudOperations = mock(classOf[CrudOperations])
  val extractDatabase: ExtractDatabase = mock(classOf[ExtractDatabase])

  when(databaseConnection.getConnection).thenReturn(mockConnection)
  def getConnection: Option[Try[Connection]] = {
    Some(Try {
      databaseConnection.getConnection
    })
  }

  Given("""^the application configuration is loaded with JDBC URL, username, and password$""") {
    () =>
      getConnection should not be empty
  }

  When("""^the application attempts to connect to the database$""") {
    () =>
      getConnection match {
        case Some(Success(conn)) =>
          logger.info("Connection has been successfully established!")
          generateEmployeeXml.insertDataIntoDataBase(conn)
          crudOperations.menu()
          extractDatabase.extractData(conn, "extracted_employees.xml")
          databaseConnection.closeConnection(conn)
        case Some(Failure(exception)) =>
          logger.error(s"Connection disrupted! ${exception.getMessage}")
        case None =>
          fail("Connection was not established")
      }
  }

  Then("""^a successful connection is established$""") {
    () =>
      getConnection should not be empty
      getConnection.get shouldBe a[Success[_]]
  }

  And("""^a log message "connection has been successfully established!" is recorded$""") {
    () =>
      verify(logger).info("Connection has been successfully established!")
  }

  Given("""^a successful database connection$""") {
    () =>
      getConnection match {
        case Some(Success(_)) =>
        case _ => fail("No successful connection")
      }
  }

  When("""^the application generates employee XML data$""") {
    () =>
     generateEmployeeXml.insertDataIntoDataBase(mockConnection)
  }

  And("""^inserts the data into the database$""") {
    () =>
      verify(generateEmployeeXml).insertDataIntoDataBase(mockConnection)
  }

  Then("""^the data should be successfully inserted into the database$""") {
    () =>
      verify(generateEmployeeXml).insertDataIntoDataBase(any[Connection])
  }

  When("""^the application displays the CRUD operations menu$""") {
    () =>
      crudOperations.menu()
  }

  Then("""^the user can perform create, read, update, and delete operations on employee data$""") {
    () =>
      verify(crudOperations, times(1)).menu()
  }

  When("""^the application extracts employee data from the database$""") {
    () =>
      extractDatabase.extractData(mockConnection, "extracted_employees.xml")
  }

  And("""^saves it to an XML file$""") {
    () =>
      verify(extractDatabase, times(1)).extractData(any[Connection], eqMatcher("extracted_employees.xml"))
  }

  Then("""^the data should be successfully saved to "extracted_employees.xml"$""") {
    () =>
      verify(extractDatabase, times(1)).extractData(any[Connection], eqMatcher("extracted_employees.xml"))
  }

  When("""^the connection fails$""") {
    () =>
      when(databaseConnection.getConnection).thenThrow(new RuntimeException("Connection failed"))
      getConnection match {
        case Some(Failure(exception)) =>
          logger.error(s"Connection disrupted! ${exception.getMessage}")
        case _ =>
          fail("Connection did not fail")
      }
  }

  Then("""^an error message "connection disrupted! <exception message>" is logged$""") {
    () =>
      getConnection match {
        case Some(Failure(exception)) =>
          verify(logger, times(1)).error(s"Connection disrupted! ${exception.getMessage}")
        case _ =>
      }
  }
}
