package ru.database

import db.mappers.AccountMapper
import db.model.Account
import ru.database.DatabaseUtil

import scala.util.{Try, Failure, Success}
import org.apache.ibatis.exceptions.PersistenceException



object AccountService {

  private def execute[T](query: Query, arg: Either[Int, Account]): QueryResult[T] = {

    val sqlSession = DatabaseUtil.createSqlSession()
    val mapper = sqlSession.getMapper(classOf[AccountMapper])

    val res = query match {
      case CreateAccount => mapper.createAccount(arg.right.get).asInstanceOf[T]
      case UpdateAccount => mapper.updateAccount(arg.right.get).asInstanceOf[T]
      case DeleteAccountById => mapper.deleteAccountById(arg.left.get).asInstanceOf[T]
      case SelectAccountById => mapper.getAccountById(arg.left.get).asInstanceOf[T]
      case SelectAllAccounts => mapper.getAllAccounts.asInstanceOf[T]
    }
    sqlSession.commit()
    sqlSession.close()

    QueryResult[T](res)

  }

  def createAccount(account: Account): Unit = {
    execute[Unit](CreateAccount, Right(account)).result
  }

  def updateAccount(account: Account): Unit = {
    execute[Unit](UpdateAccount, Right(account)).result
  }

  def deleteAccountById(id: Int): Unit = {
    execute[Unit](DeleteAccountById, Left(id)).result
  }

  def getAccountById(id: Int): Account = {
    execute[Account](SelectAccountById, Left(id)).result
  }

  def getAllAccounts(): Iterable[Account] = {
    import scala.collection.JavaConverters._
    execute[java.util.ArrayList[Account]](SelectAllAccounts, null).result.asScala
  }

  def mergeAccount(account: Account): Unit = {
    Try(execute[Unit](CreateAccount, Right(account)).result) match {
      case Success(value) => value
      case Failure(ex) => ex match {
        case ex: PersistenceException => execute[Unit](UpdateAccount, Right(account)).result
        case _ => ex
      }
    }

  }


}


