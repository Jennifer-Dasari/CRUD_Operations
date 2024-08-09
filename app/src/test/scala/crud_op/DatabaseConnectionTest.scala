//package crud_op
//
//import org.mockito.Mockito._
//import org.mockito.ArgumentMatchers._
//import org.scalatest.{FlatSpec, Matchers}
//import org.scalatestplus.mockito.MockitoSugar
//import org.slf4j.{Logger, LoggerFactory}
//
//import java.sql.{Connection, DriverManager}
//
//class DatabaseConnectionTest extends FlatSpec with Matchers with MockitoSugar {
//
//  // Mock the necessary components
//  private val mockConnection: Connection = mock[Connection]
//  private val mockLogger: Logger = mock[Logger]
//
//  // Define a test version of DatabaseConnection
//  object TestDatabaseConnection {
//    val logger: Logger = LoggerFactory.getLogger(getClass)
//    val dbUrl: String = "jdbc:sqlserver://localhost;databaseName=TestDB" // Example URL for testing
//    val username: String = "sa"
//    val password: String = "password"
//
//    def createConnection: Connection = {
//      DriverManager.getConnection(dbUrl, username, password)
//    }
//
//    def getConnection: Connection = {
//      val conn = createConnection
//      logger.info("Database connection established.")
//      conn
//    }
//
//    def closeConnection(connection: Connection): Unit = {
//      if (connection != null && !connection.isClosed) {
//        logger.info("Database connection closed.")
//        connection.close()
//      }
//    }
//  }
//
//  "DatabaseConnection" should "establish a connection successfully" in {
//    // Setup mocks with argument matchers
//    when(DriverManager.getConnection(any[String], any[String], any[String])).thenReturn(mockConnection)
//
//    // Execute the method
//    val connection = TestDatabaseConnection.getConnection
//
//    // Verify results
//    connection shouldBe mockConnection
//    verify(mockLogger).info("Database connection established.")
//    verifyNoMoreInteractions(mockLogger)
//  }
//
//  it should "close the connection properly" in {
//    // Setup mocks
//    when(mockConnection.isClosed).thenReturn(false)
//
//    // Execute the method
//    TestDatabaseConnection.closeConnection(mockConnection)
//
//    // Verify results
//    verify(mockLogger).info("Database connection closed.")
//    verify(mockConnection).close()
//    verifyNoMoreInteractions(mockLogger, mockConnection)
//  }
//
//  it should "not close an already closed connection" in {
//    // Setup mocks
//    when(mockConnection.isClosed).thenReturn(true)
//
//    // Execute the method
//    TestDatabaseConnection.closeConnection(mockConnection)
//
//    // Verify results
//    verify(mockLogger, never()).info("Database connection closed.")
//    verify(mockConnection, never()).close()
//  }
//}
