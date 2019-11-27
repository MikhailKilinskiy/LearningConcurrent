package ru.client

import scala.collection._
import scala.util.{Try, Success, Failure}
import scala.swing._
import scala.swing.event._
import javax.swing.table._
import javax.swing._
import javax.swing.border._
import java.awt.Color
import java.io.File


abstract class ClientFrame extends MainFrame with ClientLogic {
  import BorderPanel.Position._

  title = "AccountApp"

  class AccountPane extends BorderPanel {

    object scrollPane extends ScrollPane {
      val columnNames = Array[AnyRef]("Account number",
                                      "Client number",
                                      "Balance")
      val accountTable = new Table {
        showGrid = true
        model = new DefaultTableModel(columnNames, 0) {
          override def isCellEditable(r: Int, c: Int) = false
        }
        selection.intervalMode = Table.IntervalMode.Single
      }
      contents = accountTable
    }
    layout(scrollPane) = Center

    object buttons extends GridPanel(1,2) {
      val transferButton = new Button("Transfer money")

      contents += transferButton
      listenTo(transferButton)

      val fromAccountField = new TextField {
        columns = 10
        text = "From account"
      }

      val toAccountField = new TextField {
        columns = 10
        text = "To account"
      }

      val transferredSum = new TextField {
        columns = 10
        text = "Sum to transfer"
      }

      contents += fromAccountField
      contents += toAccountField
      contents += transferredSum


      reactions += {
        case ButtonClicked(component) if component == transferButton =>
          import scala.concurrent.ExecutionContext.Implicits.global
          val fut = transferMoney(fromAccountField.text.toInt, toAccountField.text.toInt, transferredSum.text.toDouble)
          fut.onComplete {
            case Success(value) =>
              swing {
                status.label.text = value
                refreshPane(accounts.accountPane)
              }
            case Failure(ex) => status.label.text = ex.getLocalizedMessage
          }
      }


    }

    layout(buttons) = South


    def table = scrollPane.accountTable

  }

  object accounts extends GridPanel(1,1) {
    val accountPane = new AccountPane
    contents += accountPane
  }

  object status extends BorderPanel {
    val label = new Label("connecting...", null, Alignment.Left)
    layout(new Label("Status: ")) = West
    layout(label) = Center
  }

  object menu extends MenuBar {
    object account extends Menu("Account") {
      val exit = new MenuItem("Exit application")
      contents += exit
    }
    object help extends Menu("Help") {
      val about = new MenuItem("About...")
      contents += about
    }
    contents += account
    contents += help
  }

  contents = new BorderPanel {
    layout(menu) = North
    layout(accounts) = Center
    layout(status) = South
  }





}




