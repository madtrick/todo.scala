package test.scala

import utest._
import main.scala.TodoItem

object TodoItemTests extends TestSuite {
  val tests = Tests {
    test("item description length") {
      intercept[IllegalArgumentException] {
        TodoItem(
          0,
          false,
          "this is a long text that should throw an exception. The max length is 50 chars"
        )
      }
    }
  }
}
