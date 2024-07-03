package services

import java.io.FileWriter
import scala.util.Random
import model.Employee

object GenerateEmployeeXml {
  def main(args: Array[String]): Unit = {
    val output_file = "employees.xml"
    val numRecords = 200000
    val names = Array("jenni", "bobby", "john", "hepshi", "david", "luna", "cynthia", "jacob", "peter", "rickey")
    val departments = Array("HR", "IT", "FINANCE", "DEVELOPER", "TECHNICAL", "SALES")
    val cities = Array("New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas")
    val states = Array("NY", "CA", "IL", "TX", "AZ", "PA")
    val writer = new FileWriter(output_file)
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
    writer.write("<employees>\n")
    for (i <- 1 to numRecords) {
      val id = i
      val name = getRandomElement(names)
      val age = Random.nextInt(50) + 10
      val department = getRandomElement(departments)
      val city = getRandomElement(cities)
      val state = getRandomElement(states)
      val employee = Employee(id, name, age, department, city, state)
      writer.write(s"<employee>\n")
      writer.write(s"<id>${employee.id}</id>\n")
      writer.write(s"<name>${employee.name}</name>\n")
      writer.write(s"<age>${employee.age}</age>\n")
      writer.write(s"<department>${employee.department}</department>\n")
      writer.write(s"<city>${employee.city}</city>\n")
      writer.write(s"</employee>\n")
    }
    writer.write("</employees>\n")
    writer.close()
    println(s"Generated $numRecords records with unique IDs in $output_file")
  }

  //function to get a random element from an array
  def getRandomElement[T](array: Array[T]): T = {
    val randomIndex = Random.nextInt(array.length)
    array(randomIndex)
  }
}