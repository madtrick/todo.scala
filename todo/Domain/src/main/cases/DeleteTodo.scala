package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait
import main.cases.errors.TodoItemNotFoundError

object DeleteTodo {
  def apply(
      id: Int,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Either[TodoItemNotFoundError, Unit] = {
    val todos = todoItemsCollection.load()

    if (todos.length == 0 || todos(0).id > id || todos.last.id < id) {
      return Left(TodoItemNotFoundError(id))
    }

    todos.find(_.id == id) match {
      case Some(_) => {
        val filteredTodos = todoItemsCollection
          .load()
          .filter((todo) => todo.id != id)

        todoItemsCollection.save(filteredTodos)
        Right(())
      }
      case None => Left(TodoItemNotFoundError(id))
    }
  }
}
