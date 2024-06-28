package repository

import java.sql.Connection

trait DataConfig {
  val url: String
  val username: String
  val password: String

  def getConnection: Connection
}

