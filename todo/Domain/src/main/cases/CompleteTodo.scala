package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait
import main.cases.errors.TaskIndexOutOfBoundariesError

object CompleteTodo {
  def apply(
      index: Int,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Either[TaskIndexOutOfBoundariesError, Unit] = {

    if (index == 0 || index > todoItemsCollection.length) {
      return Left(TaskIndexOutOfBoundariesError(index))
    }
    val todos = todoItemsCollection.load
    val todo  = todos(index - 1)
    println(s"Complete todo $todo")

    todo.completed = true

    todoItemsCollection.save(todos)
    Right(())
  }
}
