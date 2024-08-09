package crud_op

import com.typesafe.config.Config
import org.mockito.ArgumentMatchers.{any, argThat, eq => eqArg}
import org.mockito.Mockito.when
import org.scalatest.MustMatchers.convertToAnyMustWrapper
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.Logger
import services.GenerateEmployeeXml

import java.io.File

class GenerateEmployeeXmlTest extends FlatSpec with Matchers with MockitoSugar {

  private def mockConfig(filePath: String): Config = {
    val mockConfig = mock[Config]
    when(mockConfig.getString("path")).thenReturn(filePath)
    mockConfig
  }

  private def mockLogger: Logger = mock[Logger]

  "GenerateEmployeeXml" should "generate XML file if it does not exist" in {
    val filePath = "test-employees.xml"
    val config = mockConfig(filePath)
    val logger = mockLogger
    val genEmployeeXml = new GenerateEmployeeXml
    val file = new File(genEmployeeXml.filepath)
    if (file.exists()) file.delete()
    val result = genEmployeeXml.generating()
    result must be ("generated new report")
    file.exists() must be(true)
    file.delete()
  }

  it should "not generate XML file if it already exists" in {
    val filePath = "test-employees.xml"
    val config = mockConfig(filePath)
    val logger = mockLogger

    val genEmployeeXml = new GenerateEmployeeXml

    val file = new File(genEmployeeXml.filepath)
    if (!file.exists()) {
      file.createNewFile() // Ensure the file exists before running the test
    }
    val result = genEmployeeXml.generating()

    result must be("file data already existed")

    file.delete()
  }

  it should "get a random element from a list of strings" in {
    val config = mockConfig("test-employees.xml")
    val logger = mockLogger
    val genEmployeeXml = new GenerateEmployeeXml
    val list = List("a", "b", "c")
    val element = genEmployeeXml.getRandomElement(list)
    list must contain (element)
  }

  it should "get a random element within a range of integers" in {
    val config = mockConfig("test-employees.xml")
    val logger = mockLogger
    val genEmployeeXml = new GenerateEmployeeXml
    val element = genEmployeeXml.getRandomElement(1, 10)
    element must (be >= 1 and be <= 10)
  }

//      it should "insert data into database" in {
//        val filePath = "test-employees.xml"
//        val config = mockConfig(filePath)
//        val logger = mockLogger
//        val mockConnection = mock[Connection]
//        val mockDbQueryImp = mock[DbQueryImp]
//        val xmlContent =
//          """<?xml version="1.0" encoding="UTF-8"?>
//            |<employees>
//            |  <employee>
//            |    <id>1</id>
//            |    <name>jenni</name>
//            |    <age>30</age>
//            |    <department>IT</department>
//            |    <city>New York</city>
//            |    <state>NY</state>
//            |    <timestamp>2024-07-24T12:34:56</timestamp>
//            |  </employee>
//            |</employees>""".stripMargin
//        val file = new File(filePath)
//        val writer = new FileWriter(file)
//        writer.write(xmlContent)
//        writer.close()
//        val genEmployeeXml = new GenerateEmployeeXml {
//          override val config = mockConfig(filePath)
//          override val logger = mockLogger
//          override def insertDataIntoDataBase(connection: Connection): Seq[String] = {
//            val result = super.insertDataIntoDataBase(connection)
//            verify(mockDbQueryImp).insertEmployeeData(
//              eqArg(connection),
//              argThat((e: Employee) => e.id == 1 && e.name == "jenni" && e.age == 30 &&
//                e.department == "IT" && e.city == "New York" && e.state == "NY" &&
//                e.timestamp == "2024-07-24T12:34:56")
//            )
//            result
//          }
//        }
//        genEmployeeXml.insertDataIntoDataBase(mockConnection)
//        file.delete()
//      }
}
