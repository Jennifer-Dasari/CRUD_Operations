package services
import model.Employee
import repository.DatabaseConnection

import java.sql.Connection
import scala.io.StdIn
object ConsoleInterface extends App with CrudOperations {
  override def getConnection: Connection = DatabaseConnection.getConnection
  override val password: String = DatabaseConnection.password
  override val url: String = DatabaseConnection.url
  override val username: String = DatabaseConnection.username

  //starting the database connection
  val connection: Connection = DatabaseConnection.getConnection
  try{
    //confirming the existence of table
    createTable(connection)
//console interaction
    var continue = true
    while (continue){
      println("\nchoose an operation")
      println("1.insert employee")
      println("2.update employee")
      println("3.delete employee")
      println("4.view employee")
      println("5. exit")
      println("ENTER YOUR CHOICE:")
      val choice = StdIn.readInt()
      choice match {
        case 1 =>
          val employee = readEmployeeFromConsole()
          insertEmployeeData(connection, employee)
        case 2 =>
          //update the data here
        val employee = readEmployeeFromConsole()
          updateEmployeeData(connection, employee)
        case 3 =>
          //delete the data should be done here
          print("Enter the ID of the employee to delete: ")
          val id = StdIn.readInt()
          deleteEmployeeData(connection, id)
        case 4 =>
          //viewing the data
        println("Enter the ID of the employee to view the data: ")
          val id = StdIn.readInt()
          viewEmployeeData(connection, id)
        case 5 =>
          println("EXITING....")
          continue = false
        case _ =>
          println("INVALID CHOICE PLEASE GIVE VALID CHOICE: ")
      }
      }
    }finally {
    if(connection != null) connection.close()
  }
def readEmployeeFromConsole(): Employee ={
  println("\n enter the details:")
  print("ID:")
  val id = StdIn.readInt()
  print("NAME:")
  val name =StdIn.readLine()
  print("AGE:")
  val age = StdIn.readInt()
  print("DEPARTMENT:")
  val department = StdIn.readLine()
  print("SALARY:")
  val salary = StdIn.readInt()
  print("HOUSE NUMBER:")
  val hno =StdIn.readInt()
  print("STREET:")
  val street = StdIn.readLine()
  print("CITY:")
  val city = StdIn.readLine()
  print("STATE:")
  val state = StdIn.readLine()
  print("ZIPCODE:")
  val zipcode = StdIn.readInt()
  println("success")

  Employee(id,name,age,department,salary,hno, street, city, state, zipcode)
  }
}
