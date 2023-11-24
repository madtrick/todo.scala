package test.scala

import utest._
import main.scala.TodoItem

object HelloTests extends TestSuite {
  val tests = Tests {
    test("item description length") {
      intercept[IllegalArgumentException] {
        TodoItem(
          false,
          "this is a long text that should throw an exception. The max length is 50 chars"
        )
      }
    }
  }
}
