package ru.client

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout

class ClientActor(implicit val timeut: Timeout) extends Actor {
  import ClientActor._
  import ru.server.ServerActor._

  def unconnected: Actor.Receive = {
    case Start(host) =>
      val serverPath = s"akka.tcp://BankServerSystem@$host/user/server" //
      val server = context.actorSelection(serverPath)
      server ! Identify(())
      context.become(connecting(sender))
  }

  def connecting(application: ActorRef): Actor.Receive = {
    case ActorIdentity(_, Some(ref)) =>
      application ! true
      println("Connecting to: " + ref)
      context.become(connected(ref))
    case ActorIdentity(_, None) =>
      application ! false
      context.become(unconnected)
  }

  def connected(serverActor: ActorRef): Actor.Receive = {
    case command: Command => {
      serverActor ? command
    } pipeTo sender
  }

  def receive= unconnected

}


object ClientActor {
  case class Start(serverUrl: String)
}