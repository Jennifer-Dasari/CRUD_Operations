//package steps
//import io.cucumber.scala.{EN, ScalaDsl}
//import model.Employee
//import org.scalatest.Matchers
//import org.scalatestplus.mockito.MockitoSugar
//import repository.{DatabaseConnection, DbQueryImp}
//import services.ExtractDatabase
//
//import java.sql.Connection
//
//class EmployeeManagementStep extends ScalaDsl with EN with Matchers with MockitoSugar {
//
//  val databaseConnection = new DatabaseConnection
//  val dbQuery = new DbQueryImp
//  val extractDatabase = new ExtractDatabase(databaseConnection)
//  val connection: Connection = databaseConnection.getConnection
//
//  Given("""^the employee table is created$""") { () =>
//    dbQuery.createTable(connection) should equal("Table created successfully")
//  }
//
//  Given("""^I have a new employee with ID (\d+), name "([^"]*)", age (\d+), department "([^"]*)", city "([^"]*)", state "([^"]*)", and timestamp "([^"]*)"$""") {
//    (id: Int, name: String, age: Int, department: String, city: String, state: String, timestamp: String) =>
//      val employee = Employee(id, name, age, department, city, state, timestamp)
//      dbQuery.insertEmployeeData(connection, employee) should equal("Employee data inserted successfully")
//  }
//
//  When("""^I update the employee with ID (\d+)$""") { (id: Int) =>
//    val employee = dbQuery.getById(connection, id)
//    employee should not be empty
//  }
//
//  Then("""^the employee's details should successfully be updated$""") { () =>
//    val employee = dbQuery.getById(connection, 1)
//    employee should not be empty
//    employee.get.name should equal("john")
//  }
//
//  When("""^I retrieve the employee with ID (\d+)$""") { (id: Int) =>
//    val employee = dbQuery.getById(connection, id)
//    employee should not be empty
//  }
//
//  Then("""^I should get the employee's details including name, age, department, city, state, and timestamp$""") { () =>
//    val employee = dbQuery.getById(connection, 3)
//    employee should not be empty
//    employee.get.name should equal("Charlie") // Example: Adjust according to your data
//  }
//
//  Given("""^multiple employees exist in the database$""") { () =>
//    val employees = List(
//      Employee(1, "Alice", 28, "HR", "New York", "NY", "2024-08-05T12:00:00"),
//      Employee(2, "Bob", 35, "IT", "San Francisco", "CA", "2024-08-05T12:00:00"),
//      Employee(3, "Charlie", 40, "Finance", "Chicago", "IL", "2024-08-05T12:00:00"),
//      Employee(4, "Diana", 29, "Marketing", "Seattle", "WA", "2024-08-05T12:00:00")
//    )
//    employees.foreach { employee =>
//      dbQuery.insertEmployeeData(connection, employee) should equal("Employee data inserted successfully")
//    }
//  }
//
//  Given("""^the database contains employee data$""") { () =>
//    val employees = List(
//      Employee(1, "Alice", 28, "HR", "New York", "NY", "2024-08-05T12:00:00"),
//      Employee(2, "Bob", 35, "IT", "San Francisco", "CA", "2024-08-05T12:00:00"),
//      Employee(3, "Charlie", 40, "Finance", "Chicago", "IL", "2024-08-05T12:00:00"),
//      Employee(4, "Diana", 29, "Marketing", "Seattle", "WA", "2024-08-05T12:00:00")
//    )
//    employees.foreach { employee =>
//      dbQuery.insertEmployeeData(connection, employee) should equal("Employee data inserted successfully")
//    }
//  }
//
//  When("""^I extract employees data to an XML file$""") { () =>
//    val xmlFilePath = "employees.xml"
//    extractDatabase.extractData(connection, xmlFilePath) should equal("XML file created successfully")
//  }
//
//  Then("""^the XML file should be created with the correct employee data$""") { () =>
//    val xmlFilePath = "employees.xml"
//    val xmlFile = scala.xml.XML.loadFile(xmlFilePath)
//    (xmlFile \\ "employee").length should be > 0
//  }
//}
