package integration_testing

import main.MainApplication
import org.mockito.Mockito.{doNothing, verify, when}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import org.slf4j.LoggerFactory
import repository.DatabaseConnection
import services.{ConsoleUserInput, CrudOperations, ExtractDatabase, GenerateEmployeeXml}
import java.sql.Connection

class MainApplicationIntegrationTest extends FlatSpec with Matchers with MockitoSugar{
"MainApplication" should "run the main workflow correctly" in{
  val mockDatabaseConnection = mock[DatabaseConnection]
  val mockGenerateEmployeeXml = mock[GenerateEmployeeXml]
  val mockCrudOperations = mock[CrudOperations]
  val mockExtraDatabase = mock[ExtractDatabase]
  val mockLogger = LoggerFactory.getLogger(getClass)
  val mockUserInput = mock[ConsoleUserInput]
  val mockConnection = mock[Connection]

  when(mockDatabaseConnection.getConnection).thenReturn(mockConnection)
  doNothing().when(mockCrudOperations).menu()
val crudOperationFactory:Connection => CrudOperations = _ => mockCrudOperations
MainApplication.run(
  mockDatabaseConnection,
  mockGenerateEmployeeXml,
  crudOperationFactory,
  mockExtraDatabase,
  mockLogger
)
  verify(mockDatabaseConnection).getConnection
  verify(mockGenerateEmployeeXml).insertDataIntoDataBase(mockConnection)
  verify(mockCrudOperations).menu()
  verify(mockExtraDatabase).extractData(mockConnection,"extracted_employees.xml")
  verify(mockDatabaseConnection).closeConnection(mockConnection)
}
}
