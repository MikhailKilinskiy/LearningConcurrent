package ru.client

import scala.swing.SimpleSwingApplication

object AccountGUI extends SimpleSwingApplication {
  def top = new ClientFrame {
    def host = "127.0.0.1:28080"
  }

  override def main(args: Array[String]): Unit = {
    super.main(args)
  }
}
