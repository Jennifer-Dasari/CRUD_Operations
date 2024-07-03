//// File: src/main/scala/services/ExportToXML.scala
//package services
//import java.sql.Connection
//import model.Employee
//import repository.DatabaseConnection
//import scala.xml.Elem
//object ExportToXML extends App {
//  // Initialize database connection
//  val connection: Connection = DatabaseConnection.getConnection
//  try {
//    println("Fetching records from the database...")
//    // Fetch all employees from the database
//    val employees = fetchAllEmployees(connection)
//    // Convert employees to XML
//    val xmlData = <employees>{ employees.map(employeeToXml) }</employees>
//    // Write XML to file
//    val pw = new java.io.PrintWriter("employees_exported.xml")
//    pw.write(xmlData.toString())
//    pw.close()
//    println("Finished writing records to employees_exported.xml.")
//  } finally {
//    if (connection != null) connection.close()
//  }
//  def fetchAllEmployees(connection: Connection): List[Employee] = {
//    val statement = connection.createStatement()
//    val resultSet = statement.executeQuery("SELECT id, name, age, department, salary, hno, street, city, state, zipcode FROM Employees")
//    var employees: List[Employee] = List()
//    while (resultSet.next()) {
//      val employee = Employee(
//        resultSet.getInt("id"),
//        resultSet.getString("name"),
//        resultSet.getInt("age"),
//        resultSet.getString("department"),
//        resultSet.getInt("salary"),
//        resultSet.getInt("hno"),
//        resultSet.getString("street"),
//        resultSet.getString("city"),
//        resultSet.getString("state"),
//        resultSet.getInt("zipcode")
//      )
//      employees = employees :+ employee
//    }
//    resultSet.close()
//    statement.close()
//    employees
//  }
//  def employeeToXml(employee: Employee): Elem = {
//    <employee>
//      <id>{employee.id}</id>
//      <name>{employee.name}</name>
//      <age>{employee.age}</age>
//      <department>{employee.department}</department>
//      <salary>{employee.salary}</salary>
//      <hno>{employee.hno}</hno>
//      <street>{employee.street}</street>
//      <city>{employee.city}</city>
//      <state>{employee.state}</state>
//      <zipcode>{employee.zipcode}</zipcode>
//    </employee>
//  }
//}
