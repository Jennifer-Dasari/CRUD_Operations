package crud_op

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import services.ExtractDatabase

import java.io.File
import java.sql.{Connection, ResultSet, Statement}
import scala.xml.XML

class ExtractDatabaseTest extends FlatSpec with Matchers with MockitoSugar {
  "ExtractDatabase" should "correctly extract data and write it into XML file" in {
    val mockConnection: Connection = mock[Connection]
    val mockStatement: Statement = mock[Statement]
    val mockResult: ResultSet = mock[ResultSet]

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
    ExtractDatabase.extractData(mockConnection,outputFile.getAbsolutePath)
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
          |<name>Employee(1,joe,22,tech,hyd,TS,23s).name</name>
          |<age>Employee(1,joe,22,tech,hyd,TS,23s).age</age>
          |<department>Employee(1,joe,22,tech,hyd,TS,23s).department</department>
          |<city>Employee(1,joe,22,tech,hyd,TS,23s).city</city>
          |<state>Employee(1,joe,22,tech,hyd,TS,23s).state</state>
          |<timestamp>23s</timestamp>
          |</employee>
          |<employee>
          |<id>2</id>
          |<name>Employee(2,max,18,sales,knl,AP,24s).name</name>
          |<age>Employee(2,max,18,sales,knl,AP,24s).age</age>
          |<department>Employee(2,max,18,sales,knl,AP,24s).department</department>
          |<city>Employee(2,max,18,sales,knl,AP,24s).city</city>
          |<state>Employee(2,max,18,sales,knl,AP,24s).state</state>
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

