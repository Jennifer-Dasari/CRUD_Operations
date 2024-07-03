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
