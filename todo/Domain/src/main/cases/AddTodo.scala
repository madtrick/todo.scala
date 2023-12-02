package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait

object AddTodo {
  def apply(
      todo: String,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Unit = {
    println(s"Add todo $todo")

    val item  = TodoItem(false, todo)
    val todos = todoItemsCollection.load() :+ item

    todoItemsCollection.save(todos)
  }
}
