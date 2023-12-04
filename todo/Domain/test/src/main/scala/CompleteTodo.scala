package main.scala

import utest._
import main.scala.TodoItem
import main.cases.AddTodo
import java.nio.file.Paths
import main.cases.CompleteTodo

object CompleteTodoTest extends TestSuite {
  val tests = Tests {
    test("complete todo") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      TodoItemsCollection.save(List(new TodoItem(false, "paint the porch")))

      var todo = TodoItemsCollection.load()(0)

      CompleteTodo(1, TodoItemsCollection)

      todo = TodoItemsCollection.load()(0)

      assert(todo.completed)
    }
  }
}
