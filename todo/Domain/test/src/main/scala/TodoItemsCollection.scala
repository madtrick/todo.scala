package main.scala

import utest._
import java.nio.file.Paths
import scala.util.Random

object TodoItemsCollectionTest extends TestSuite {
  val tests = Tests {
    test("collection length") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      assert(TodoItemsCollection.length == 0)

      TodoItemsCollection.save(List(new TodoItem(false, "take the trash out")))

      assert(TodoItemsCollection.length == 1)
    }

    test("save an item to the collection") {
      TodoItemsCollection.filepath = Paths.get(s"/tmp/${TestUtils.todoFileName}")

      TodoItemsCollection.save(List(new TodoItem(false, "clean the garage")))

      val todo = TodoItemsCollection.load()(0)

      assert(todo.completed == false)
      assert(todo.action == "clean the garage")
    }
  }
}
