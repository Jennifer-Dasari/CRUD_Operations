package services
import repository.{DataConfig, DatabaseConnection}
import model.Employee
import java.sql.{Connection, DriverManager, SQLException}
object EmployeeOperations extends DataConfig with CrudOperations {
  // Implementing DataConfig members
  override val url: String = DatabaseConnection.url
  override val username: String = DatabaseConnection.username
  override val password: String = DatabaseConnection.password
  // Implementing getConnection method
  def getConnection: Connection = {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    DriverManager.getConnection(url, username, password)
  }
  def main(args: Array[String]): Unit = {
    var connection: Connection = null
    try {
      connection = getConnection // Accessing getConnection method from DataConfig
      createTable(connection) // Create Employees table if it doesn't exist
      // Sample employee data
      val employee1 = Employee(1, "bobby", 21, "sales", 12345, 101, "parklane", "hyd", "telangana", 518006)
      val employee2 = Employee(2, "rema", 22, "marketing", 123445, 102, "downstreet", "hyderabad", "telangana", 500007)
      val employee3 = Employee(3, "rama", 24, "IT", 13452, 104, "lawcollege", "bangalore", "karnataka", 100003)
      val employee4 = Employee(4, "latha", 25, "tech", 123467, 106, "manikonda", "hyderabad", "telangana", 500008)
      // Insert employees data
      insertEmployeeData(connection, employee1)
      println(s"Inserted employee: $employee1")
      insertEmployeeData(connection, employee2)
      println(s"Inserted employee: $employee2")
      insertEmployeeData(connection, employee3)
      println(s"Inserted employee: $employee3")
      insertEmployeeData(connection, employee4)
      println(s"Inserted employee: $employee4")

      // update the exixting data
      val updatingEmployee = employee1.copy(city = "kammam",department = "tech", id = 5)
      updateEmployeeData(connection,employee1)
      println(s"updated data: $updatingEmployee")
      //deleting employee data
      deleteEmployeeData(connection,employee3.id)
      println(s"deleted employee: ${employee3.id}")
      viewEmployeeData(connection,employee1.id)
    } catch {
      case e: SQLException => e.printStackTrace()
    } finally {
      if (connection != null) connection.close() // Close the database connection
    }
  }
}
