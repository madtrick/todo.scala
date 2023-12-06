package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait
import main.cases.errors.TodoItemNotFoundError

object CompleteTodo {
  def apply(
      index: Int,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Either[TodoItemNotFoundError, Unit] = {

    if (index == 0 || index > todoItemsCollection.length) {
      return Left(TodoItemNotFoundError(index))
    }
    val todos = todoItemsCollection.load
    val todo  = todos(index - 1)
    println(s"Complete todo $todo")

    todo.completed = true

    todoItemsCollection.save(todos)
    Right(())
  }
}
