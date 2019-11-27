package ru.client

import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor._
import akka.pattern.{ask, pipe}
import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import scala.swing._
import scala.swing.event._
import javax.swing.table._
import javax.swing._
import javax.swing.border._

import db.model.Account
import ru.server.ServerActor


trait ClientLogic {
  self: ClientFrame =>

  implicit val timeout: Timeout = Timeout(5 seconds)
  val system = ru.remotingSystem("ClientSystem", 0)
  val clientActor = system.actorOf(Props(classOf[ClientActor], timeout))

  def host: String

  def showAccounts(): Future[Array[Account]] = {
    val f = clientActor ? ServerActor.GetAccountList
    f.mapTo[Array[Account]]
  }

  def transferMoney(fromAccId: Int, toAccId: Int, sum: Double): Future[String] = {
    val f = clientActor ? ServerActor.Transfer(fromAccId, toAccId, sum)
    f.mapTo[Try[String]].map {
      case Success(value) => value
      case Failure(ex) => throw ex
    }
  }

  val connected: Future[Boolean] = {
    val f = clientActor ? ClientActor.Start(host)
    f.mapTo[Boolean]
  }

  def swing(body: => Unit) = {
    val r = new Runnable { def run() = body }
    javax.swing.SwingUtilities.invokeLater(r)
  }

  private def toRow(acc: Account): Array[AnyRef] = {
    Array[AnyRef](acc.getAccount_id.toString,
      acc.getClient_id.toString,
      acc.getBalance)
  }

  def refreshPane(pane: AccountPane): Unit = {

    def update(accs: Array[Account]): Unit = {
      val table = pane.scrollPane.accountTable
      table.model match {
        case d: DefaultTableModel =>
          d.setRowCount(0)
          accs.foreach{a => d.addRow(toRow(a))}
      }
    }

    showAccounts onComplete {
        case Success(accs) => swing{update(accs)}
        case Failure(ex) => swing{status.label.text = "Could not update account list"}
      }
  }


   connected.onComplete {
    case Success(true) =>
      swing {
        status.label.text= "Connected!"
        refreshPane(accounts.accountPane)
      }
    case Success(false) =>
      swing { status.label.text = "Could not find server." }
    case Failure(t) =>
      println(t.getMessage)
      swing { status.label.text = s"Could not connect to server: $t" }
  }

}
