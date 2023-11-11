package example

import org.rogach.scallop._
import java.io._
import upickle.default.{ReadWriter, macroRW, write, read}
import java.nio.file.Files
import java.nio.file.Paths
import scala.io.Source

case class TodoItem(var completed: Boolean, action: String) {
  require(action.length() < 50, "Item action can't be longer than 50 chars")
}

object TodoItem {
  implicit val rw: ReadWriter[TodoItem] = macroRW
}

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  object add extends Subcommand("add") {
    val item = trailArg[String]()
  }

  object list extends Subcommand("list") {}

  object complete extends Subcommand("complete") {
    val index = opt[Int](required = true)
  }

  addSubcommand(add)
  addSubcommand(list)
  addSubcommand(complete)

  verify()
}

@main def todo(args: String*) = {
  val conf = new Conf(args)

  val fileExists = Files.exists(Paths.get("./todo.json"))
  // TODO: Replace `List` with a type that provides better by-index access
  var todos: List[TodoItem] = List()

  if fileExists then todos = read(Source.fromFile("./todo.json").mkString)

  conf.subcommand match
    case Some(conf.add) => {

      val writer = new PrintWriter(new File("todo.json"))
      val item   = TodoItem(false, conf.add.item())

      todos = todos :+ item

      val json = write(todos)

      writer.write(json)
      writer.close()

    }
    case Some(conf.list) => {
      todos.zipWithIndex.foreach((task, index) => {
        val status = if (task.completed) "completed" else "pending"
        println(s"[$index] Task: \"${task.action}\" Status: $status")
      })
    }
    case Some(conf.complete) => {
      val index = conf.complete.index()

      if index > todos.length then
        println(s"Task index out of boundaries")
        sys.exit(-1)

      val todo = todos(index - 1)
      todo.completed = true

      val writer = new PrintWriter(new File("todo.json"))
      val json   = write(todos)

      writer.write(json)
      writer.close()

    }
    case _ => println("Unknown mode")
}
