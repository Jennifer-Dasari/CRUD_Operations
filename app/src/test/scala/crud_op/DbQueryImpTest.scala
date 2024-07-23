package crud_op

import model.Employee
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import repository.DbQueryImp

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}

class DbQueryImpTest extends FlatSpec with Matchers with MockitoSugar{
  "DbQureyImp" should " create a table" in {
    val mockConnection:Connection = mock[Connection]
    val mockStatement:Statement = mock[Statement]
    when(mockConnection.createStatement()).thenReturn(mockStatement)
    when(mockStatement.executeUpdate(any[String])).thenReturn(1)
    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.createTable(mockConnection)
    result shouldBe 1
  }
  it should "insert an employee" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatement:PreparedStatement = mock[PreparedStatement]
    val employee = Employee(1, "jenni",22,"developer","HYD","TS","23s")
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.executeUpdate()).thenReturn(1)
    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.insertEmployeeData(mockConnection,employee)
    result shouldBe "Employee data inserted successfully"
  }
  it should "update an employee by an id" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatement:PreparedStatement = mock[PreparedStatement]
    val employee = Employee(1, "jenni",22,"developer","HYD","TS","23s")
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.executeUpdate()).thenReturn(1)
    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.updateById(mockConnection,1,employee)
    result shouldBe s"Employee updated successfully"
  }
  it should "delete am employee data by an Id" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatments:PreparedStatement = mock[PreparedStatement]
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatments)
    when(mockPreparedStatments.executeUpdate()).thenReturn(1)

    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.deleteById(mockConnection,1)
    result shouldBe "Employee deleted successfully"
  }
  it should "get an employee by id" in {
    val mockConnection:Connection = mock[Connection]
    val mockPreparedStatments:PreparedStatement = mock[PreparedStatement]
    val mockResultSet:ResultSet = mock[ResultSet]
    val employee = Employee(1,"jenni",22,"developer","hyd","TS","23s")
    when(mockConnection.prepareStatement(any[String])).thenReturn(mockPreparedStatments)
    when(mockPreparedStatments.executeQuery()).thenReturn(mockResultSet)
    when(mockResultSet.next()).thenReturn(true)
    when(mockResultSet.getInt("Id")).thenReturn(employee.id)
    when(mockResultSet.getString("Name")).thenReturn(employee.name)
    when(mockResultSet.getInt("Age")).thenReturn(employee.age)
    when(mockResultSet.getString("Department")).thenReturn(employee.department)
    when(mockResultSet.getString("City")).thenReturn(employee.city)
    when(mockResultSet.getString("State")).thenReturn(employee.state)
    when(mockResultSet.getString("Timestamp")).thenReturn(employee.timestamp)
    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.getById(mockConnection,1)
    result shouldBe Some(employee)
  }
  it should "retrive all the employee data" in {
    val mockConnection:Connection = mock[Connection]
    val mockStatments:Statement = mock[Statement]
    val mockResultSet:ResultSet = mock[ResultSet]
    val employee1 = Employee(1,"jenni",22,"developer","hyd","TS","23s")
    val employee2 = Employee(2,"john",17,"tech","hyd","TS","23s")
    when(mockConnection.createStatement()).thenReturn(mockStatments)
    when(mockStatments.executeQuery(any[String])).thenReturn(mockResultSet)
    when(mockResultSet.next()).thenReturn(true,true,false)
    when(mockResultSet.getInt("Id")).thenReturn(employee1.id,employee2.id)
    when(mockResultSet.getString("Name")).thenReturn(employee1.name,employee2.name)
    when(mockResultSet.getInt("Age")).thenReturn(employee1.age,employee2.age)
    when(mockResultSet.getString("Department")).thenReturn(employee1.department,employee2.department)
    when(mockResultSet.getString("City")).thenReturn(employee1.city,employee2.city)
    when(mockResultSet.getString("State")).thenReturn(employee1.state,employee2.state)
    when(mockResultSet.getString("Timestamp")).thenReturn(employee1.timestamp,employee2.timestamp)
    val dbQueryImp = new DbQueryImp()
    val result = dbQueryImp.getAll(mockConnection)
    result shouldBe List(employee1, employee2)
  }

}
