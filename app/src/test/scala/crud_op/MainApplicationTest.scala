//package crud_op
//
////import com.typesafe.scalalogging.Logger
//import main.MainApplication
//import org.mockito.Mockito._
//import org.scalatest.{FlatSpec, Matchers}
//import org.scalatestplus.mockito.MockitoSugar
//import org.slf4j.Logger
//import repository.DatabaseConnection
//import services.{CrudOperations, ExtractDatabase, GenerateEmployeeXml}
//
//import java.sql.Connection
//
//class MainApplicationTest extends FlatSpec with Matchers with MockitoSugar {
//
//  val mockLogger:Logger = mock[Logger]
//  val mockDatabaseConnection :DatabaseConnection = mock[DatabaseConnection]
//  val mockConnection:Connection = mock[Connection]
//  val mockGeneratingEmployeeXml:GenerateEmployeeXml = mock[GenerateEmployeeXml]
//  val mockCrudOperations:CrudOperations = mock[CrudOperations]
//  val mockExtractDatabase:ExtractDatabase = mock[ExtractDatabase]
//  "Main Application " should "successfully establish connection" in {
//    when(mockDatabaseConnection.getConnection).thenReturn(mockConnection)
//    when(mockGeneratingEmployeeXml.insertDataIntoDataBase(mockConnection)).thenReturn(Seq("data has been inserted"))
//    doNothing().when(mockCrudOperations).menu()
//    doNothing().when(mockExtractDatabase).extractData(mockConnection,"extracted_employee.xml")
//    MainApplication.main(Array.empty)
////    verify(mockGeneratingEmployeeXml).insertDataIntoDataBase(mockConnection)
////    verify(mockCrudOperations).menu()
////    verify(mockExtractDatabase).extractData(mockConnection,"extracted_employees.xml")
////    verify (mockLogger).info("connection has been successfully established!")
//}
//}




package crud_op

import main.MainApplication
import org.mockito.Mockito._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import org.slf4j.Logger
import repository.DatabaseConnection
import services.{ConsoleUserInput, CrudOperations, ExtractDatabase, GenerateEmployeeXml}

import java.sql.Connection

class MainApplicationTest extends FlatSpec with Matchers with MockitoSugar {

  val mockLogger: Logger = mock[Logger]
  val mockDatabaseConnection: DatabaseConnection = mock[DatabaseConnection]
  val mockConnection: Connection = mock[Connection]
  val mockGeneratingEmployeeXml: GenerateEmployeeXml = mock[GenerateEmployeeXml]
  val mockCrudOperations: CrudOperations = mock[CrudOperations]
  val mockExtractDatabase: ExtractDatabase = mock[ExtractDatabase]
  val mockUserInput: ConsoleUserInput = mock[ConsoleUserInput]

  "Main Application " should "successfully establish connection" in {
    when(mockDatabaseConnection.getConnection).thenReturn(mockConnection)
    when(mockGeneratingEmployeeXml.insertDataIntoDataBase(mockConnection)).thenReturn(Seq("data has been inserted"))
    doNothing().when(mockCrudOperations).menu()
    doNothing().when(mockExtractDatabase).extractData(mockConnection, "extracted_employees.xml")

    MainApplication.run(
      mockDatabaseConnection,
      mockGeneratingEmployeeXml,
      _ => mockCrudOperations,
      mockExtractDatabase,
      mockLogger
    )
    verify(mockDatabaseConnection).getConnection
    verify(mockGeneratingEmployeeXml).insertDataIntoDataBase(mockConnection)
    verify(mockCrudOperations).menu()
    verify(mockExtractDatabase).extractData(mockConnection, "extracted_employees.xml")
    verify(mockLogger).info("connection has been successfully established!")
  }
  it should "handle connection failure" in{
    val mockLogger: Logger = mock[Logger]
    val mockDatabaseConnection: DatabaseConnection = mock[DatabaseConnection]
    val mockGenerateEmployeeXml: GenerateEmployeeXml = mock[GenerateEmployeeXml]
    val mockExtractDatabase: ExtractDatabase = mock[ExtractDatabase]
    val exception = new RuntimeException("Connection error")
    when(mockDatabaseConnection.getConnection).thenThrow(exception)

    MainApplication.run(
      mockDatabaseConnection,
      mockGenerateEmployeeXml,
      _ => mock[CrudOperations],
      mockExtractDatabase,
      mockLogger
    )
    verify(mockDatabaseConnection).getConnection
    verify(mockLogger).error(s"connection disrupted! ${exception.getMessage}")
  }
  it should "execute main method correctly" in {
    MainApplication.main(Array.empty)
  }
}
