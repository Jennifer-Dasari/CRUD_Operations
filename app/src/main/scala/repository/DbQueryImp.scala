package repository

import model.Employee

import java.sql.Connection

class DbQueryImp extends DbQuery {
  def createTable(connection: Connection) = {
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

  def insertEmployeeData(connection: Connection, employee: Employee) = {
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

  def updateEmployeeData(connection: Connection, employee: Employee) = {
    val updateData = connection.prepareStatement("UPDATE Employees SET name = ?, age = ?, department = ?,salary = ?,hno = ?, street = ?,city = ?,state = ?, zipcode = ? WHERE id = ?")
    updateData.setString(1, employee.name)
    updateData.setInt(2, employee.age)
    updateData.setString(3, employee.department)
    updateData.setInt(4, employee.salary)
    updateData.setInt(5, employee.hno)
    updateData.setString(6, employee.street)
    updateData.setString(7, employee.city)
    updateData.setString(8, employee.state)
    updateData.setInt(9, employee.zipcode)
    updateData.setInt(10, employee.id)
    updateData.executeUpdate()
    updateData.close()
    println(s"Updated employee: $employee")
  }

  def deleteEmployeeData(connection: Connection, id: Int) = {
    val deleteData = connection.prepareStatement("DELETE FROM Employees WHERE id = ?")
    deleteData.setInt(1, id)
    deleteData.executeUpdate()
    deleteData.close()
  }

  def viewEmployeeData(connection: Connection, id: Int) = {
    val sql = "SELECT * FROM Employees WHERE id = ?"
    val statement = connection.prepareStatement(sql)
    statement.setInt(1, id)
    val result = statement.executeQuery()
    if (result.next()) {
      val employee = Employee(
        result.getInt("id"),
        result.getString("name"),
        result.getInt("age"),
        result.getString("department"),
        result.getInt("salary"),
        result.getInt("hno"),
        result.getString("street"),
        result.getString("city"),
        result.getString("state"),
        result.getInt("zipcode")
      )
      println(employee)
    } else {
      println(s"No employee found with ID: $id")
    }
    result.close()
    statement.close()
  }
}