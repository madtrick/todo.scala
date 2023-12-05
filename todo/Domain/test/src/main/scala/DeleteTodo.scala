package main.scala

import utest._
import main.scala.TodoItem
import main.cases.DeleteTodo
import java.nio.file.Paths
import main.cases.errors.TaskIndexOutOfBoundariesError
import main.cases.AddTodo

object DeleteTodoTest extends TestSuite {
  val tests = Tests {
    test("deletes a todo") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      DeleteTodo(1, TodoItemsCollection)

      assert(TodoItemsCollection.length == 0)
    }
  }
}
