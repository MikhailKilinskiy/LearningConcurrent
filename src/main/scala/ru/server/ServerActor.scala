package ru.server

import scala.util.Try
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor._
import akka.pattern.pipe
import akka.event.Logging


class ServerActor(accountSystem: AccountSystem) extends Actor {
  import ServerActor._

  val log = Logging(context.system, this)

  def receive = {

    case Transfer(fromAccId, toAccId, sum) =>
      Future {
        Try(accountSystem.transfer(fromAccId, toAccId, sum))
      } pipeTo sender

    case GetAccountList =>
      val accList = accountSystem.getAccountList()
      sender ! accList
  }
}


object ServerActor {
  sealed trait Command
  case class Transfer(fromAccId: Int, toAccId: Int, sum: Double) extends Command
  case object GetAccountList extends Command

  def apply(accountSystem: AccountSystem) = Props(classOf[ServerActor], accountSystem)
}
