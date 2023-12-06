package main.scala

import utest._
import main.scala.TodoItem
import main.cases.DeleteTodo
import java.nio.file.Paths
import main.cases.errors.TodoItemNotFoundError
import main.cases.AddTodo

object DeleteTodoTest extends TestSuite {
  val tests = Tests {
    test("deletes a todo") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      DeleteTodo(0, TodoItemsCollection)

      assert(TodoItemsCollection.length == 0)
    }

    test("returns an error if the id does not exist (empty list)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      val result = DeleteTodo(0, TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

    test("returns an error if the id does not exist (non empty list, over)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      val result = DeleteTodo(3, TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

    test("returns an error if the id does not exist (non empty list, under)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      // Create the TodoItem directly (not via AddTodo) to create it with a higher id
      TodoItemsCollection.save(List(new TodoItem(3, false, "buy a lottery ticket")))

      val result = DeleteTodo(1, TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

    test("returns an error if the id does not exist (non empty list, middle)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      // Create the TodoItem directly (not via AddTodo) to create it with a higher and lower id
      TodoItemsCollection.save(
        List(new TodoItem(0, false, "buy a lottery ticket"), new TodoItem(3, false, "buy a coffee"))
      )

      val result = DeleteTodo(1, TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }
  }
}
