package ru.server

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.collection._
import scala.concurrent.duration._
import scala.util.Try
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object BankServer {
  def main(args: Array[String]): Unit = {
    import ServerActor._

    val accountSystem = new AccountSystem()
    accountSystem.init()

    val port =  28080// args(0).toInt
    val actorSystem = ru.remotingSystem("BankServerSystem", port)
    val serverActor = actorSystem.actorOf(ServerActor(accountSystem), "server")
  /*
    implicit val timeout = Timeout(5 seconds)
    val transaction = Transfer(1, 3, 10)
    val future: Future[Any] = serverActor ? transaction
    val result = Await.result(future, timeout.duration)
    println(result)

   */




  }

}
