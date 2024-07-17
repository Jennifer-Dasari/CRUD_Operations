package repository

import main.MainApplication.logger
import model.Employee
import java.sql.{Connection, PreparedStatement, SQLException, Statement}

class DbQueryImp extends DbQuery {
  def createTable(connection: Connection): Int = {
    try {
      val statement = connection.createStatement()
      val dropTable = "IF OBJECT_ID('Employees', 'U') IS NOT NULL DROP TABLE Employees"
      statement.execute(dropTable)
      val createSqlTable = {
        """
          |CREATE TABLE Employees(
          | id INT Identity(1,1) PRIMARY KEY,
          | name VARCHAR(20),
          | age INT,
          | department VARCHAR(20),
          | city VARCHAR(20),
          | state VARCHAR(20),
          | timestamp VARCHAR(20)
          |);
        """.stripMargin
      }
      statement.executeUpdate(createSqlTable)
    } catch {
      case e: Exception => logger.error("Error creating table: " + e.getMessage)
        0
    }
  }

  def insertEmployeeData(connection: Connection, employee: Employee): String = {
    try {
      val insertSql: String =
        """
          |SET IDENTITY_INSERT Employees ON
          |INSERT INTO Employees (ID, Name, Age, Department, City, State, timestamp) VALUES (?,?,?,?,?,?,?)""".stripMargin
      val preparedStatement = connection.prepareStatement(insertSql)
      preparedStatement.setInt(1, employee.id)
      preparedStatement.setString(2, employee.name)
      preparedStatement.setInt(3, employee.age)
      preparedStatement.setString(4, employee.department)
      preparedStatement.setString(5, employee.city)
      preparedStatement.setString(6, employee.state)
      preparedStatement.setString(7, employee.timestamp)
      preparedStatement.executeUpdate()
      preparedStatement.close()
      "Employee data inserted successfully"
    } catch {
      case e: SQLException if e.getErrorCode == 2627 =>
        "DUP"
      case e: Exception =>
        logger.error(s"SQL ERROR OCCURED: ${e.getMessage}")
//        e.printStackTrace()
        "DUP"
    }
  }

   def updateById(connection: Connection, id: Int, employee: Employee): String = {
    try {
      val updateData = connection.prepareStatement(
        "UPDATE Employees SET name = ?, age = ?, department = ?, city = ?, state = ?, timestamp = ? WHERE id = ?"
      )
      updateData.setString(1, employee.name)
      updateData.setInt(2, employee.age)
      updateData.setString(3, employee.department)
      updateData.setString(4, employee.city)
      updateData.setString(5, employee.state)
      updateData.setString(6, employee.timestamp)
      updateData.setInt(7, id)

      val rowsUpdated = updateData.executeUpdate()
      updateData.close()

      if (rowsUpdated > 0) s"Employee $id updated successfully"
      else s"No employee found with ID $id"
    } catch {
      case e: Exception =>
//        e.printStackTrace()
        "Error updating employee: " + e.getMessage
    }
  }

  def deleteById(connection: Connection, id: Int): String = {
    try {
      val deleteData = connection.prepareStatement("DELETE FROM Employees WHERE id = ?")
      deleteData.setInt(1, id)
      val rowsDeleted = deleteData.executeUpdate()
      deleteData.close()

      if (rowsDeleted > 0) s"Employee with ID $id deleted successfully"
      else s"No employee found with ID $id"
    } catch {
      case e: Exception =>
//        e.printStackTrace()
        "Error deleting employee: " + e.getMessage
    }
  }

  def getById(connection: Connection, id: Int): Option[Employee] = {
    try {
      val query = "SELECT * FROM Employees WHERE ID = ?"
      val preparedStatement: PreparedStatement = connection.prepareStatement(query)
      preparedStatement.setInt(1, id)
      val resultSet = preparedStatement.executeQuery()
      if (resultSet.next()) {
        val employee = Employee(
          resultSet.getInt("ID"),
          resultSet.getString("Name"),
          resultSet.getInt("Age"),
          resultSet.getString("Department"),
          resultSet.getString("City"),
          resultSet.getString("State"),
          resultSet.getString("timestamp")
        )
        Some(employee)
      } else {
        None
      }
    } catch {
      case e: Exception =>
//        e.printStackTrace()
        None
    }
  }

  def getAll(connection: Connection): List[Employee] = {
    try {
      val query = "SELECT * FROM Employees"
      val statement: Statement = connection.createStatement()
      val resultSet = statement.executeQuery(query)
      val employees = Iterator.continually(resultSet).takeWhile(_.next()).map { rs =>
        Employee(
          rs.getInt("ID"),
          rs.getString("Name"),
          rs.getInt("Age"),
          rs.getString("Department"),
          rs.getString("City"),
          rs.getString("State"),
          rs.getString("timestamp")
        )
      }.toList
      statement.close()
      employees
    } catch {
      case e: Exception =>
//        e.printStackTrace()
        List.empty
    }
  }
}
