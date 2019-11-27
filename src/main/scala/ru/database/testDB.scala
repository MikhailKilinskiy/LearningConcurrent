package ru.database

import java.sql.DriverManager

object testDB {
  val DB_URL = "jdbc:postgresql://localhost:5555/postgres"
  val USER = "postgres"
  val PASS = "postgres"

  def main(args: Array[String]): Unit = {
    try {
      Class.forName("org.postgresql.Driver")
    } catch {
      case e: ClassNotFoundException => e.printStackTrace()
    }

    val connection = DriverManager.getConnection(DB_URL, USER, PASS)
    if (connection != null) {
      println("Sucessful!")
    }

    val stmt = connection.createStatement

    val sql =
      """
        |CREATE TABLE IF NOT EXISTS Account(
        |   account_id serial PRIMARY KEY,
        |   client_id integer NOT NULL,
        |   balance decimal(18,2) NOT NULL
        |);
      """.stripMargin

    stmt.executeUpdate(sql)


  }

}


