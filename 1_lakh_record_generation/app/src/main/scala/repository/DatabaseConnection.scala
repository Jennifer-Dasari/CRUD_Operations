package repository
import com.typesafe.config.ConfigFactory
import java.sql.{Connection, DriverManager}
object DatabaseConnection {
  private val config = ConfigFactory.load().getConfig("database")
  private val dbUrl = config.getString("Url")
  private val username = config.getString("username")
  private val password = config.getString("password")
  def getConnection: Connection = {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    DriverManager.getConnection(dbUrl, username, password)
  }
}

































































//package repository
//import java.sql.{Connection, DriverManager,ResultSet}
//trait DataConfig{
//  val url:String
//  val username: String
//  val password: String
//}
//object DatabaseConnection extends DataConfig{
//  override val url: String = "jdbc:sqlserver://DESKTOP-C1QO8OE\\SQLEXPRESS;databaseName=EMPLOYEEDATABASE"
//  override val username: String = "sa"
//  override val password: String = "admin"
//  def getConnection: Connection ={
//    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
//    DriverManager.getConnection(url,username,password)
//  }
//  def getCount(connection: Connection): Int = {
//    val statement = connection.createStatement()
//    val resultSet: ResultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM Employees")
//    resultSet.next()
//    val count = resultSet.getInt("count")
//    resultSet.close()
//    statement.close()
//    count
//  }
//  def main(args: Array[String]): Unit = {
//    val connection = getConnection
//    try {
//      val count = getCount(connection)
//      println(s"Total number of records in Employees table: $count")
//    } finally {
//      connection.close()
//    }
//  }
//}