package main.scala

import utest._
import java.nio.file.Paths
import scala.util.Random

object TodoItemsCollectionTest extends TestSuite {
  val tests = Tests {
    test("collection length") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      assert(TodoItemsCollection.length == 0)

      TodoItemsCollection.save(List(new TodoItem(0, false, "take the trash out")))

      assert(TodoItemsCollection.length == 1)
    }

    test("saves an item to the collection") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      TodoItemsCollection.save(List(new TodoItem(0, false, "clean the garage")))

      val todo = TodoItemsCollection.load()(0)

      assert(todo.id == 0)
      assert(todo.completed == false)
      assert(todo.action == "clean the garage")
    }

    test("returns the next id for an item (empty list)") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      assert(TodoItemsCollection.nextId == 0)
    }

    test("returns the next id for an item") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      assert(TodoItemsCollection.nextId == 0)

      TodoItemsCollection.save(
        List(new TodoItem(0, false, "learn german"), new TodoItem(1, false, "dust the bedroom"))
      )

      assert(TodoItemsCollection.nextId == 2)

      TodoItemsCollection.save(List(new TodoItem(4, false, "dust the bedroom")))

      assert(TodoItemsCollection.nextId == 5)
    }
  }
}
