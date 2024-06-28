
package repository
import java.sql.{Connection, DriverManager}
object DatabaseConnection extends DataConfig {
  override val url: String = "jdbc:sqlserver://DESKTOP-C1QO8OE\\SQLEXPRESS;databaseName=EMPLOYEEDATABASE"
  override val username: String = "sa"
  override val password: String = "admin"
  def getConnection: Connection = {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    DriverManager.getConnection(url, username, password)
  }
}
