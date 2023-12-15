package main.cases.errors

final case class TodoItemNotFoundError(id: Int) {
  val kind = TodoItemNotFoundError.kind
}

object TodoItemNotFoundError {
  val kind = "TodoItemNotFoundError"
}
