package repository

import com.typesafe.config.ConfigFactory
import main.MainApplication.logger

import java.sql.{Connection, DriverManager}

object DatabaseConnection {
  private val config = ConfigFactory.load().getConfig("database")
  private val dbUrl: String = config.getString("jdbcUrl")
  private val username: String = config.getString("username")
  private val password: String = config.getString("password")

  def getConnection: Connection = {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    DriverManager.getConnection(dbUrl, username, password)
  }

  def closeConnection(connection: Connection) = {
    if (connection != null && !connection.isClosed) {
      connection.close()
      logger.info("Database connection closed.")
    }
  }
}