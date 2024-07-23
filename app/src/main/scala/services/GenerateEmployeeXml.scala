package services

import com.typesafe.config.ConfigFactory
import model.Employee
import org.slf4j.LoggerFactory
import repository.DbQueryImp

import java.io.{File, FileWriter}
import java.sql.Connection
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.Random
import scala.xml.XML

class GenerateEmployeeXml extends DbQueryImp {
  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load().getConfig("filepath")
  val filepath = config.getString("path")

  def generating(): String = {
    if (filepath.isEmpty) {
      val numRecords = 200000
      val names = List("jenni", "bobby", "john", "hepshi", "david", "luna", "cynthia", "jacob", "peter", "rickey")
      val departments = List("HR", "IT", "FINANCE", "DEVELOPER", "TECHNICAL", "SALES")
      val cities = List("New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas")
      val states = List("NY", "CA", "IL", "TX", "AZ", "PA")
      val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
      val writer = new FileWriter(filepath)
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
      writer.write("<employees>\n")

      for (i <- 1 to numRecords) {
        val id = i
        val name = getRandomElement(names)
        val age = getRandomElement(21, 60)
        val department = getRandomElement(departments)
        val city = getRandomElement(cities)
        val state = getRandomElement(states)
        val timestamp = dateFormat.format(new Date())

        writer.write(s"<employee>\n")
        writer.write(s"<id>${id}</id>\n")
        writer.write(s"<name>${name}</name>\n")
        writer.write(s"<age>${age}</age>\n")
        writer.write(s"<department>${department}</department>\n")
        writer.write(s"<city>${city}</city>\n")
        writer.write(s"<state>${state}</state>\n")
        writer.write(s"<timestamp>$timestamp </timestamp>\n")
        writer.write(s"</employee>\n")
      }
      writer.write("</employees>\n")
      writer.close()
      logger.info(s"Generated $numRecords records with unique IDs in $filepath")
      "generated new report"
    } else {
      logger.info("file already generated")
      "file data already existed"
    }
  }

  def getRandomElement(List: List[String]): String = {
    val randomIndex = Random.nextInt(List.length)
    List(randomIndex)
  }

  def getRandomElement(min: Int, max: Int): Int = {
    Random.nextInt(max - min + 1) + min
  }

  def insertDataIntoDataBase(connection: Connection): Seq[String] = {
    generating()
    createTable(connection)
    val dbQuery = new DbQueryImp
    val xml = XML.loadFile(new File(filepath))
    val result = (xml \\ "employee").map { employeeNode =>
      val id = (employeeNode \ "id").text.toInt
      val name = (employeeNode \ "name").text
      val age = (employeeNode \ "age").text.toInt
      val department = (employeeNode \ "department").text
      val city = (employeeNode \ "city").text
      val state = (employeeNode \ "state").text
      val timestamp = (employeeNode \ "timestamp").text
      val employee = Employee(id, name, age, department, city, state, timestamp)

      dbQuery.insertEmployeeData(connection, employee)
    }
    logger.info("data has been inserted !")
    result
  }
}
