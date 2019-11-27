package ru.server

import scala.concurrent.stm._

import db.model.Account
import ru.database.AccountService
import scala.util.{Try, Success, Failure}


class AccountSystem {
  private final val accounts = TMap[Int, Ref[Account]]()

  def init()= atomic { implicit txn =>
    accounts.clear()

    val accountIterator = AccountService.getAllAccounts()
    accountIterator.foreach{
      acc => accounts(acc.getAccount_id) = Ref(acc)
    }
  }

  def getAccountList(): Array[Account] = {
    accounts.snapshot.map {case (key, value) => value.single()}.toArray
  }

  def transfer(fromAccountId: Int, toAccountId: Int, sum: Double): String = atomic {
    implicit txn =>
      val fromAcc = accounts(fromAccountId)
      val toAcc = accounts(toAccountId)
      if(fromAcc().getBalance <= sum) return s"No money on account ${fromAcc().getAccount_id.toString}"
      Try {
        fromAcc().setBalance(fromAcc().getBalance - sum)
        toAcc().setBalance(toAcc().getBalance + sum)
        List[Account](fromAcc(), toAcc()).foreach { acc => AccountService.updateAccount(acc) }
      } match {
        case Success(value) => s"$sum USD transferred from account ${fromAcc().getAccount_id} to account ${toAcc().getAccount_id}"
        case Failure(ex) => throw ex
      }


  }


}




