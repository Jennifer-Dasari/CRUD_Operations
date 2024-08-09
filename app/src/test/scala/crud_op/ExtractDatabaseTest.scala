package crud_op

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.Logger
import repository.DatabaseConnection
import services.ExtractDatabase

import java.io.File
import java.sql.{Connection, ResultSet, Statement}
import scala.xml.XML

class ExtractDatabaseTest extends FlatSpec with Matchers with MockitoSugar {
  "ExtractDatabase" should "correctly extract data and write it into XML file" in {
    val mockConnection: Connection = mock[Connection]
    val mockStatement: Statement = mock[Statement]
    val mockResult: ResultSet = mock[ResultSet]
    val databaseConnection = mock[DatabaseConnection]
    val logger: Logger = mock[Logger]

    when(mockResult.next()).thenReturn(true, true,false)
    when(mockResult.getInt("Id")).thenReturn(1, 2)
    when(mockResult.getString("Name")).thenReturn("joe", "max")
    when(mockResult.getInt("Age")).thenReturn(22, 18)
    when(mockResult.getString("Department")).thenReturn("tech", "sales")
    when(mockResult.getString("City")).thenReturn("hyd", "knl")
    when(mockResult.getString("State")).thenReturn("TS", "AP")
    when(mockResult.getString("Timestamp")).thenReturn("23s", "24s")

    when(mockConnection.createStatement()).thenReturn(mockStatement)
    when(mockStatement.executeQuery(any[String])).thenReturn(mockResult)
    val outputFile = new File("src/test/resources/test_extracted_employees.xml")
    if (outputFile.exists()) outputFile.delete()

    println("Starting extraction...")
    new ExtractDatabase(databaseConnection,logger).extractData(mockConnection,outputFile.getAbsolutePath)
    println(s"File exists: ${outputFile.exists()}")

    if (outputFile.exists()) {
      val source = scala.io.Source.fromFile(outputFile)
      val fileContent = source.getLines().mkString("\n")
      source.close()
      println("File content:")
      println(fileContent)

      val expectedXml =
        """<?xml version="1.0" encoding="UTF-8"?>
          |<employees>
          |<employee>
          |<id>1</id>
          |<name>joe</name>
          |<age>22</age>
          |<department>tech</department>
          |<city>hyd</city>
          |<state>TS</state>
          |<timestamp>23s</timestamp>
          |</employee>
          |<employee>
          |<id>2</id>
          |<name>max</name>
          |<age>18</age>
          |<department>sales</department>
          |<city>knl</city>
          |<state>AP</state>
          |<timestamp>24s</timestamp>
          |</employee>
          |</employees>""".stripMargin

      val expectedXmlContent = XML.loadString(expectedXml)
      val fileXmlContent = XML.loadFile(outputFile)
      fileXmlContent shouldEqual expectedXmlContent
    }else{
      fail("the output file was not created.")
    }
    outputFile.delete()
  }

}