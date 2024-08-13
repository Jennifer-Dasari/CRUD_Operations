package crud_op

import model.Employee
import org.mockito.Mockito.{verify, when}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import org.slf4j.Logger
import repository.DbQueryImp
import services.{ConsoleUserInput, CrudOperations, UserInput}

import java.sql.Connection

class CrudOperationsTest extends FlatSpec with Matchers with MockitoSugar {

  val mockLogger: Logger = mock[Logger]
  val mockRepo: DbQueryImp = mock[DbQueryImp]
  val mockConnection: Connection = mock[Connection]
  val mockUserInput: UserInput = mock[ConsoleUserInput]

  val crudOps: CrudOperations = new CrudOperations(mockConnection, mockUserInput) {
    override val logger: Logger = mockLogger

    override def insertEmployeeData(connection: Connection, employee: Employee): String = {
      mockRepo.insertEmployeeData(connection, employee)
    }

    override def updateById(connection: Connection, id: Int, employee: Employee): String = {
      mockRepo.updateById(connection, id, employee)
    }

    override def deleteById(connection: Connection, id: Int): String = {
      mockRepo.deleteById(connection, id)
    }

    override def getById(connection: Connection, id: Int): Option[Employee] = {
      mockRepo.getById(connection, id)
    }

    override def getAll(connection: Connection): List[Employee] = {
      val employees = mockRepo.getAll(connection)
      employees.foreach(employee => logger.info(s"Employee details: $employee"))
      employees
    }
  }

  "CrudOperations" should "log menu options when menu() is called" in {
    when(mockUserInput.readLine("")).thenReturn("6") // Simulate user choosing "Exit"

    crudOps.menu() // Triggering the menu

    verify(mockLogger).info("Choose an operation:")
    verify(mockLogger).info("1. Insert employee")
    verify(mockLogger).info("2. Update employee")
    verify(mockLogger).info("3. Delete employee")
    verify(mockLogger).info("4. Get employee by ID")
    verify(mockLogger).info("5. Get all employees")
    verify(mockLogger).info("6. Exit")
    verify(mockLogger).info("Enter your choice:")
  }

  it should "insert an employee and log the result" in {
    val employee = Employee(1, "John Doe", 30, "IT", "New York", "NY", "2023-01-01T00:00:00")
    when(mockUserInput.readLine("Enter employee ID:")).thenReturn("1")
    when(mockUserInput.readLine("Enter employee name:")).thenReturn("John Doe")
    when(mockUserInput.readLine("Enter employee age:")).thenReturn("30")
    when(mockUserInput.readLine("Enter employee department:")).thenReturn("IT")
    when(mockUserInput.readLine("Enter employee city:")).thenReturn("New York")
    when(mockUserInput.readLine("Enter employee state:")).thenReturn("NY")
    when(mockUserInput.readLine("inserted time:")).thenReturn("2023-01-01T00:00:00")

    when(mockRepo.insertEmployeeData(mockConnection, employee)).thenReturn("Employee data inserted successfully")

    crudOps.insertEmployee() // Triggering the  insertion

    verify(mockLogger).info("Employee data inserted successfully")
  }

  it should "update an employee and log the result" in {
    val employee = Employee(1, "Jane Doe", 28, "HR", "San Francisco", "CA", "2023-01-02T00:00:00")
    when(mockUserInput.readLine("Enter employee ID:")).thenReturn("1")
    when(mockUserInput.readLine("Enter employee name:")).thenReturn("Jane Doe")
    when(mockUserInput.readLine("Enter employee age:")).thenReturn("28")
    when(mockUserInput.readLine("Enter employee department:")).thenReturn("HR")
    when(mockUserInput.readLine("Enter employee city:")).thenReturn("San Francisco")
    when(mockUserInput.readLine("Enter employee state:")).thenReturn("CA")
    when(mockUserInput.readLine("inserted time:")).thenReturn("2023-01-02T00:00:00")

    when(mockRepo.updateById(mockConnection, 1, employee)).thenReturn("Employee update is successful")

    crudOps.updateById() // Triggering the  update, jenni mind this, u got error here

    verify(mockLogger).info("Employee update is successful")
  }

  it should "delete an employee and log the result" in {
    when(mockUserInput.readLine("Enter employee ID to delete:")).thenReturn("1")
    when(mockRepo.deleteById(mockConnection, 1)).thenReturn("Employee data successfully deleted")

    crudOps.deleteById() // Trigger deletion

    verify(mockLogger).info("Employee data successfully deleted")
  }

  it should "retrieve an employee by ID and log the result" in {
    when(mockUserInput.readLine("Enter employee ID to retrieve:")).thenReturn("1")
    val employee = Employee(1, "Mark Smith", 35, "Finance", "Chicago", "IL", "2023-01-03T00:00:00")
    when(mockRepo.getById(mockConnection, 1)).thenReturn(Some(employee))

    crudOps.getById() // Trigger retrieval

    verify(mockLogger).info(s"Employee details: $employee")
  }

  it should "log all employee data when getAll() is called" in {
    // Create mock data
    val employees = List(
      Employee(1, "John Doe", 30, "IT", "New York", "NY", "2023-01-01T00:00:00"),
      Employee(2, "Jane Smith", 25, "Finance", "Los Angeles", "CA", "2023-01-02T00:00:00")
    )

    // Mock the getAll method to return the mock data
    when(mockRepo.getAll(mockConnection)).thenReturn(employees)

    // Call getAll
    crudOps.getAll(mockConnection) // Ensure this method is called in the test

    // Verify that each employee's details were logged
    employees.foreach { emp =>
      verify(mockLogger).info(s"Employee details: $emp")
    }
  }


  it should "handle exceptions and log error messages" in {

    when(mockUserInput.readLine("")).thenThrow(new RuntimeException("Simulated exception"))

    crudOps.menu()

    verify(mockLogger).error("An error occurred during CRUD operations: Simulated exception")
  }

}
