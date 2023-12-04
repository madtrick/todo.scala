package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait
import main.cases.errors.TaskIndexOutOfBoundariesError

object DeleteTodo {
  def apply(
      index: Int,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Either[TaskIndexOutOfBoundariesError, Unit] = {

    if (index == 0 || index > todoItemsCollection.length) {
      return Left(TaskIndexOutOfBoundariesError(index))
    }

    val todos = todoItemsCollection
      .load()
      .zipWithIndex
      .filterNot({ case (task, i) => (index - 1) == i })
      .map({ case (task, index) => task })

    todoItemsCollection.save(todos)
    Right(())
  }
}
