//import com.typesafe.config.Config
//import org.mockito.ArgumentMatchers.any
//import org.mockito.Mockito.{doNothing, times, verify, when}
//import org.scalatest.mockito.MockitoSugar
//import org.scalatest.{FlatSpec, Matchers}
//import org.slf4j.Logger
//import repository.DbQueryImp
//import services.GenerateEmployeeXml
//
//import java.io.{File, FileWriter}
//import java.sql.{Connection, Statement}
//import scala.xml.{Elem, XML}
//
//class GenerateEmployeeXmlTest extends FlatSpec with Matchers with MockitoSugar {
//
//  trait TestSetup {
//    val mockLogger: Logger = mock[Logger]
//    val mockConfig: Config = mock[Config]
//    val mockConnection: Connection = mock[Connection]
//    val mockStatement: Statement = mock[Statement]
//    val mockFileWriter: FileWriter = mock[FileWriter]
//    val mockDbQuery: DbQueryImp = mock[DbQueryImp]
//
//    // Mock the behavior of FileWriter
//    when(mockFileWriter.write(any[String])).thenAnswer(_ => Unit)
//    when(mockFileWriter.close()).thenAnswer(_ => Unit)
//
//    val generateEmployeeXml = new GenerateEmployeeXml {
//      override val logger: Logger = mockLogger
//      override val config: Config = mockConfig
//    }
//
//    // Prepare a test file path
//    val filepath = "testfile.xml"
//    when(mockConfig.getString("path")).thenReturn(filepath)
//  }
//
//  "GenerateEmployeeXml" should "generate new data if the file is empty" in new TestSetup {
//    // Simulate empty file scenario
//    when(new File(filepath).exists()).thenReturn(false)
//
//    val result = generateEmployeeXml.generating()
//    verify(mockLogger, times(1)).info(s"Generated 200000 records with unique IDs in $filepath")
//    result shouldBe "generated new report"
//  }
//
//  it should "return 'file data already existed' if filepath is not empty" in new TestSetup {
//    // Simulate existing file scenario
//    when(new File(filepath).exists()).thenReturn(true)
//
//    val result = generateEmployeeXml.generating()
//    verify(mockLogger, times(1)).info("file already generated")
//    result shouldBe "file data already existed"
//  }
//
//  it should "insert data into the database" in new TestSetup {
//    val mockXml: Elem = <employees>
//      <employee>
//        <id>1</id>
//        <name>jenni</name>
//        <age>22</age>
//        <department>developer</department>
//        <city>hyd</city>
//        <state>TS</state>
//        <timestamp>23s</timestamp>
//      </employee>
//    </employees>
//
//    // Mock XML loading
//    when(XML.loadFile(new File(filepath))).thenReturn(mockXml)
//    doNothing().when(mockDbQuery).createTable(any[Connection])
//
//    val result = generateEmployeeXml.insertDataIntoDataBase(mockConnection)
//    verify(mockLogger, times(1)).info("data has been inserted !")
//    result should not be empty
//  }
//}
