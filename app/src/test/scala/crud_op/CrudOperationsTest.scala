package crud_op

import model.Employee
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import repository.DbQuery
import services.CrudOperations

import java.sql.Connection

class CrudOperationsTest extends FlatSpec with Matchers with MockitoSugar {
  val repo:DbQuery = mock[DbQuery]
  val connection:Connection = mock[Connection]
  val service:CrudOperations = new CrudOperations(connection){
    override def insertEmployeeData(connection: Connection,employee: Employee):String = {
      repo.insertEmployeeData(connection, employee)
    }
    override def createTable(connection:Connection):Int = {
      repo.createTable(connection)
    }
    override def updateById(connection: Connection,id:Int,employee: Employee):String ={
      repo.updateById(connection,id, employee)
    }

    override def deleteById(connection: Connection, id: Int): String = {
      repo.deleteById(connection, id)
    }

    override def getById(connection: Connection, id: Int): Option[Employee] = {
      repo.getById(connection, id)
    }

    override def getAll(connection: Connection): List[Employee] = {
      repo.getAll(connection)
    }
  }

  "CrudOperations" should "create a new employee" in {
    val exampleEmployee = Employee(1, "jenni", 30, "teches", "hyd", "TS", "23s")
    when(repo.insertEmployeeData(connection,exampleEmployee)).thenReturn("Employee data inserted successfully")
    val result = service.insertEmployeeData(connection,exampleEmployee)
    result shouldEqual "Employee data inserted successfully"
    verify(repo).insertEmployeeData(connection,exampleEmployee)
  }
  it should "create a table" in {
    when(repo.createTable(connection)).thenReturn(1)
    val result = service.createTable(connection)
    result shouldEqual 1
    verify(repo).createTable(connection)
  }
  it should "update an existing employee" in {
    val id = 1
    val updateEmployee =Employee(id,"john",18,"sales","kadapa","AP","21s")
    when(repo.updateById(connection,id,updateEmployee)).thenReturn("Employee update is successfull")
    val result = service.updateById(connection,id,updateEmployee)
    result shouldEqual  "Employee update is successfull"
    verify(repo).updateById(connection,id,updateEmployee)
  }
  it should "delete an employee data" in{
    val id = 1
    when(repo.deleteById(connection, id)).thenReturn("Employee data successfully deleted")
    val result = service.deleteById(connection, id)
    result shouldEqual "Employee data successfully deleted"
    verify(repo).deleteById(connection, id)
  }
  it should "display employee data by id" in {
    val id = 1
    val exampleEmploy = Option(Employee(1,"mark",23,"mech","hyd","TS","21s"))
    when(repo.getById(connection, id)).thenReturn(exampleEmploy)
    val result = service.getById(connection, id)
    result shouldEqual exampleEmploy
    verify(repo).getById(connection, id)
  }
  it should "display all the employee data" in {
    val employees = List(Employee(1,"name",22,"department","city","state","timestamp"),
      Employee(2,"name",21,"department","city","state","timestamp"))
    when(repo.getAll(connection)).thenReturn(employees)
    val result = service.getAll(connection)
    result shouldEqual employees
    verify(repo).getAll(connection)
  }
}