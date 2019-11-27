package ru.database

import java.io.Reader
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder

object DatabaseUtil {
  private val reader = Resources.getResourceAsReader("mybatis-config.xml")
  private val factory = new SqlSessionFactoryBuilder().build(reader)


  def createSqlSession(): SqlSession = {
    factory.openSession()
  }

}
