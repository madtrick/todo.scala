package main.scala

import utest._
import main.scala.TodoItem
import main.cases.AddTodo
import java.nio.file.Paths

object AddTodoTest extends TestSuite {
  val tests = Tests {
    test("adds a new todo item") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      AddTodo("take the trash out", TodoItemsCollection)

      val todo = TodoItemsCollection.load()(0)

      assert(todo.action == "take the trash out")
      assert(todo.completed == false)
    }

    test("appends a new todo item") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")
      TodoItemsCollection.save(List(new TodoItem(false, "clean the litter")))

      AddTodo("take the trash out", TodoItemsCollection)

      val todos  = TodoItemsCollection.load()
      val first  = todos(0)
      val second = todos(1)

      assert(TodoItemsCollection.length == 2)
      assert(first.completed == false)
      assert(first.action == "clean the litter")
      assert(second.action == "take the trash out")
      assert(second.completed == false)
    }
  }
}
