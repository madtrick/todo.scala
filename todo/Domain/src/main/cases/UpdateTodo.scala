package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait
import main.cases.errors.TodoItemNotFoundError

object UpdateTodo {
  def apply(
      id: Int,
      completed: Boolean,
      action: String,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Either[TodoItemNotFoundError, Unit] = {
    val todos = todoItemsCollection.load()

    if (todos.length == 0 || todos(0).id > id || todos.last.id < id) {
      return Left(TodoItemNotFoundError(id))
    }

    val todo = todos.find(_.id == id)

    todo match {
      case Some(todo) => {

        todo.completed = completed
        todo.action = action

        /* The "todos" list has a reference to the todo
         * we updated. No need to generate a new list */
        todoItemsCollection.save(todos)

        Right(())
      }
      case None => Left(TodoItemNotFoundError(id))
    }
  }
}
