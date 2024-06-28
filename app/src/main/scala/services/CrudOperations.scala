package services
import model.Employee
import repository.DataConfig
import java.sql.Connection
trait CrudOperations extends DataConfig {
  def createTable(connection: Connection): Unit = {
    val statement = connection.createStatement()
    val dropTable = "IF OBJECT_ID('Employees', 'U') IS NOT NULL DROP TABLE Employees"
    statement.execute(dropTable)
    val createSqlTable =
      """
        |CREATE TABLE Employees(
        | id INT PRIMARY KEY,
        | name VARCHAR(50),
        | age INT,
        | department VARCHAR(50),
        | salary INT,
        | hno INT,
        | street VARCHAR(50),
        | city VARCHAR(50),
        | state VARCHAR(50),
        | zipcode INT
        |)
      """.stripMargin
    statement.execute(createSqlTable)
    statement.close()
  }

  def insertEmployeeData(connection: Connection, employee: Employee): Unit = {
    val insertData = connection.prepareStatement("INSERT INTO Employees(id, name, age, department, salary, hno, street, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
    insertData.setInt(1, employee.id)
    insertData.setString(2, employee.name)
    insertData.setInt(3, employee.age)
    insertData.setString(4, employee.department)
    insertData.setInt(5, employee.salary)
    insertData.setInt(6, employee.hno)
    insertData.setString(7, employee.street)
    insertData.setString(8, employee.city)
    insertData.setString(9, employee.state)
    insertData.setInt(10, employee.zipcode)
    insertData.executeUpdate()
    insertData.close()
  }

  def updateEmployeeData(connection: Connection, employee: Employee): Unit = {
    val updateData = connection.prepareStatement("UPDATE Employees SET city = ?, department = ? WHERE id = ?")
    updateData.setString(1, employee.city)
    updateData.setString(2, employee.department)
    updateData.setInt(3, employee.id)
    updateData.executeUpdate()
    updateData.close()
    println(s"Updated employee: $employee")

  }

  def deleteEmployeeData(connection: Connection, id: Int): Unit = {
    val deleteData = connection.prepareStatement("DELETE FROM Employees WHERE id = ?")
    deleteData.setInt(3, id)
    deleteData.executeUpdate()
    deleteData.close()
  }
  def viewEmployeeData(connection: Connection, id: Int): Unit = {
    val sql = "SELECT * FROM Employees WHERE id = ?"
    val statement = connection.prepareStatement(sql)
    statement.setInt(1, id)
    val resultSet = statement.executeQuery()
    if (resultSet.next()) {
      val employee = Employee(
        resultSet.getInt("id"),
        resultSet.getString("name"),
        resultSet.getInt("age"),
        resultSet.getString("department"),
        resultSet.getInt("salary"),
        resultSet.getInt("hno"),
        resultSet.getString("street"),
        resultSet.getString("city"),
        resultSet.getString("state"),
        resultSet.getInt("zipcode")
      )
      println(s"Employee details: $employee")
    } else {
      println(s"No employee found with ID: $id")
    }
    resultSet.close()
    statement.close()
  }
}

