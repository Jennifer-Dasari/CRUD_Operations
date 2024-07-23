package crud_op

import model.Employee
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import repository.DbQuery

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import scala.collection.mutable.ListBuffer

class DbQueryTest extends FlatSpec with Matchers with MockitoSugar {
  trait TestDbQuery extends DbQuery {
    override def createTable(connection: Connection): Int = {
      val statement = connection.createStatement()
      statement.executeUpdate("CREATE TABLE Employees( id INT Identity(1,1) PRIMARY KEY, name VARCHAR(20), age INT, department VARCHAR(20), city VARCHAR(20), state VARCHAR(20), timestamp VARCHAR(20))")
    }

    override def insertEmployeeData(connection: Connection, employee: Employee): String = {
      val preparedStatement = connection.prepareStatement("INSERT INTO Employees (ID, Name, Age, Department, City, State, timestamp) VALUES (?,?,?,?,?,?,?)")
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
    }

    override def updateById(connection: Connection, id: Int, employee: Employee): String = {
      val updateDate = connection.prepareStatement("UPDATE Employees SET name = ?, age = ?, department = ?, city = ?, state = ?, timestamp = ? WHERE id = ?")
      updateDate.setString(1, employee.name)
      updateDate.setInt(2, employee.age)
      updateDate.setString(3, employee.department)
      updateDate.setString(4, employee.city)
      updateDate.setString(5, employee.state)
      updateDate.setString(6, employee.timestamp)
      updateDate.setInt(7, id)
      updateDate.executeUpdate()
      "employee data successfully updated"
    }

    override def deleteById(connection: Connection, id: Int): String = {
      val deleteDate = connection.prepareStatement("DELETE FROM Employees WHERE id = ?")
      deleteDate.setInt(1, id)
      deleteDate.executeUpdate()
      "employee data successfully deleted !"
    }

    def getById(connection: Connection, id: Int): Option[Employee] = {
      val preparedStatement = connection.prepareStatement("SELECT * FROM Employees WHERE ID = ?")
      preparedStatement.setInt(1, id)
      val resultSet = preparedStatement.executeQuery()
      if (resultSet.next()) {
        Some(Employee(
          resultSet.getInt("Id"),
          resultSet.getString("Name"),
          resultSet.getInt("Age"),
          resultSet.getString("Department"),
          resultSet.getString("City"),
          resultSet.getString("State"),
          resultSet.getString("Timestamp")))

      } else {
        None
      }
    }

    def getAll(connection: Connection): List[Employee] = {
      val statement = connection.createStatement()
      val resultSet =statement.executeQuery("SELECT * FROM Employees")
      val employees = ListBuffer[Employee]()
      while(resultSet.next()){
        val employee = Employee(
          resultSet.getInt("Id"),
          resultSet.getString("Name"),
          resultSet.getInt("Age"),
          resultSet.getString("Department"),
          resultSet.getString("City"),
          resultSet.getString("State"),
          resultSet.getString("Timestamp")
        )
        employees += employee
      }
employees.toList
    }
  }
  "DbQuery" should "create a table" in {
    val mockConnection:Connection = mock[Connection]
    val mockStatement:Statement = mock[Statement]

    when(mockConnection.createStatement()).thenReturn(mockStatement)
    when(mockStatement.executeUpdate(any[String])).thenReturn(1)

    val dbQuery = new TestDbQuery {

    }
    val result = dbQuery.createTable(mockConnection)
    result shouldBe 1
  }

  it should "insert an employee" in{
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatement:PreparedStatement = mock[PreparedStatement]
    val employee = Employee(1,"John Doe", 30, "Engineering", "New York", "NY", "2023")
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.executeUpdate()).thenReturn(1)
    val dbQuery = new TestDbQuery {}
    val result = dbQuery.insertEmployeeData(mockConnection,employee)
    result shouldBe "Employee data inserted successfully"
  }

  it should "update an employee by id" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatement:PreparedStatement = mock[PreparedStatement]
    val employee = Employee(1,"John Doe", 30, "Engineering", "New York","NY","2023")
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.executeUpdate()).thenReturn(1)
    val dbQuery = new TestDbQuery {}
    val result = dbQuery.updateById(mockConnection,1,employee)
    result shouldBe "employee data successfully updated"
  }
  it should "delete an employee by id" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatement:PreparedStatement = mock[PreparedStatement]
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.executeUpdate()).thenReturn(1)
    val dbQuery = new TestDbQuery {}
    val result = dbQuery.deleteById(mockConnection,1)
    result shouldBe "employee data successfully deleted !"
  }

  it should "get the employee data by id " in {
    val mockConnections:Connection = mock[Connection]
    val mockPreparedStatements:PreparedStatement = mock[PreparedStatement]
    val mockResultSet:ResultSet = mock[ResultSet]
    when(mockConnections.prepareStatement(any[String])).thenReturn(mockPreparedStatements)
    when(mockPreparedStatements.executeQuery()).thenReturn(mockResultSet)
    when(mockResultSet.next()).thenReturn(true)
    when(mockResultSet.getInt("Id")).thenReturn(1)
    when(mockResultSet.getString("Name")).thenReturn("jenni")
    when(mockResultSet.getInt("Age")).thenReturn(22)
    when(mockResultSet.getString("Department")).thenReturn("developer")
    when(mockResultSet.getString("City")).thenReturn("hyd")
    when(mockResultSet.getString("State")).thenReturn("TS")
    when(mockResultSet.getString("Timestamp")).thenReturn("23s")
    val dbQuery = new TestDbQuery {}
    val result = dbQuery.getById(mockConnections,1)
    result shouldBe Some(Employee(1,"jenni",22,"developer","hyd","TS","23s"))
  }

  it should "retrive all the employee data" in {
    val mockConnection:Connection = mock[Connection]
    val mockStatement:Statement = mock[Statement]
    val mockResultSet:ResultSet = mock[ResultSet]
    when(mockConnection.createStatement()).thenReturn(mockStatement)
    when(mockStatement.executeQuery(any[String])).thenReturn(mockResultSet)
    when(mockResultSet.next()).thenReturn(true,false)
    when(mockResultSet.getInt("Id")).thenReturn(1)
    when(mockResultSet.getString("Name")).thenReturn("jenni")
    when(mockResultSet.getInt("Age")).thenReturn(22)
    when(mockResultSet.getString("Department")).thenReturn("developer")
    when(mockResultSet.getString("City")).thenReturn("hyd")
    when(mockResultSet.getString("State")).thenReturn("TS")
    when(mockResultSet.getString("Timestamp")).thenReturn("23s")
    val dbQuery = new TestDbQuery {}
    val result = dbQuery.getAll(mockConnection)
    result shouldBe List(Employee(1,"jenni",22,"developer","hyd","TS","23s"))

  }

}