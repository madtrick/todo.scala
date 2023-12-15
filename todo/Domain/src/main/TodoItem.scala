package main.scala

import upickle.default.{ReadWriter, macroRW}
import org.joda.time.DateTime

case class TodoItem(val id: Int, var completed: Boolean, var action: String) {
  val createdAt: DateTime = DateTime.now()

  require(action.length() < 50, "Item action can't be longer than 50 chars")
}

object TodoItem {
  implicit val rw: ReadWriter[TodoItem] = macroRW
}
