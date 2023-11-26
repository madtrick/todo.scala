package main.scala

import upickle.default.{ReadWriter, macroRW}

case class TodoItem(var completed: Boolean, action: String) {
  require(action.length() < 50, "Item action can't be longer than 50 chars")
}

object TodoItem {
  implicit val rw: ReadWriter[TodoItem] = macroRW
}
