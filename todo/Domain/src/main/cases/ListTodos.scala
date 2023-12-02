package main.cases

import main.scala.TodoItemsCollectionTrait

object ListTodos {
  def apply(
      todoItemsCollection: TodoItemsCollectionTrait
  ): Unit = {
    todoItemsCollection
      .load()
      .zipWithIndex
      .foreach({
        case (task, index) => {
          val status = if (task.completed) "completed" else "pending"
          println(s"[${index + 1}] Task: \"${task.action}\" Status: $status")
        }
      })
  }
}
