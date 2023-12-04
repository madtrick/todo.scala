package main.cases

import main.scala.TodoItem
import main.scala.TodoItemsCollectionTrait

object CompleteTodo {
  def apply(
      index: Int,
      todoItemsCollection: TodoItemsCollectionTrait
  ): Unit = {

    if (index > todoItemsCollection.length) {
      println(s"Task index out of boundaries")
      sys.exit(-1)
    }
    val todos = todoItemsCollection.load
    val todo  = todos(index - 1)
    println(s"Complete todo $todo")

    todo.completed = true

    todoItemsCollection.save(todos)
  }
}
