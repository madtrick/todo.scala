package main.cases.errors

final case class TaskIndexOutOfBoundariesError(index: Int) {
  val kind = TaskIndexOutOfBoundariesError.kind
}

object TaskIndexOutOfBoundariesError {
  val kind = "TaskIndexOutOfBoundariesError"
}
