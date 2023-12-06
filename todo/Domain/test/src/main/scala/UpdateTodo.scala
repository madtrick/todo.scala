package main.scala

import utest._
import main.scala.TodoItem
import java.nio.file.Paths
import main.cases.errors.TodoItemNotFoundError
import main.cases.AddTodo
import main.cases.UpdateTodo

object UpdateTodoTest extends TestSuite {
  val tests = Tests {
    test("updates a todo") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      UpdateTodo(0, false, "paint the porch in red", TodoItemsCollection)

      val todo = TodoItemsCollection.load()(0)

      assert(todo.id == 0)
      assert(todo.action == "paint the porch in red")
      assert(todo.completed == false)
    }

    test("returns an error if the id does not exist (empty list)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      val result = UpdateTodo(0, false, "paint the porch in red", TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

    test("returns an error if the id does not exist (non empty list, over)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("paint the porch", TodoItemsCollection)

      val result = UpdateTodo(3, false, "paint the porch in red", TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

    test("returns an error if the id does not exist (non empty list, under)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      // Create the TodoItem directly (not via AddTodo) to create it with a higher id
      TodoItemsCollection.save(List(new TodoItem(3, false, "buy a lottery ticket")))

      val result = UpdateTodo(0, false, "paint the porch in red", TodoItemsCollection)

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

      val result = UpdateTodo(1, false, "paint the porch in red", TodoItemsCollection)

      result match {
        case Left(value) => assert(value.kind == TodoItemNotFoundError.kind)
        case Right(_)    => assert(false)
      }
    }

  }
}
