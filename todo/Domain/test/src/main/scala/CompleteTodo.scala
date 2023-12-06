package main.scala

import utest._
import main.scala.TodoItem
import java.nio.file.Paths
import main.cases.CompleteTodo
import main.cases.errors.TodoItemNotFoundError
import main.cases.AddTodo

object CompleteTodoTest extends TestSuite {
  val tests = Tests {
    test("completes a todo") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      CompleteTodo(1, TodoItemsCollection)

      val todo = TodoItemsCollection.load()(0)

      assert(todo.completed)
    }

    test("returns an error if the index is out of boundaries") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      val error = CompleteTodo(0, TodoItemsCollection)

      // Tried using "assertMatch" but it types the value as "Any"
      error match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case _           => assert(false)
      }
    }
  }
}
