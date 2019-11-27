package ru

package object database {

  sealed trait Query
  case object CreateAccount extends Query
  case object UpdateAccount extends Query
  case object SelectAccountById extends Query
  case object DeleteAccountById extends Query
  case object SelectAllAccounts extends Query

  case class QueryResult[T](result: T)

}
