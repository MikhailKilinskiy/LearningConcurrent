package ru.database

import db.mappers.AccountMapper
import db.model.Account
import ru.database.AccountService

object testMyBatis {
  def main(args: Array[String]): Unit = {

    println(AccountService.getAccountById(6))

    val acc = new Account(6, 4, 100.00)
    /*
    val accs = AccountService.getAllAccounts()
    accs.foreach(println)
    */

    AccountService.mergeAccount(acc)
    println(AccountService.getAccountById(6))


  }

}
