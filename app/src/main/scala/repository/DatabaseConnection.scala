package repository

import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}

import java.sql.{Connection, DriverManager}

class DatabaseConnection {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  val config: Config = ConfigFactory.load().getConfig("database")
  private val dbUrl: String = config.getString("jdbcUrl")
  private val username: String = config.getString("username")
  private val password: String = config.getString("password")

  def createConnection: Connection = {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    DriverManager.getConnection(dbUrl, username, password)
  }

  def getConnection: Connection = {
    val conn = createConnection
    logger.info("Database connection established.")
    conn
  }

  def closeConnection(connection: Connection): Unit = {
    if (connection != null && !connection.isClosed) {
      logger.info("Database connection closed.")
      connection.close()
    }
  }
}
