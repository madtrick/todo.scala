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

  object delete extends Subcommand("delete") {
    val index = opt[Int](required = true)
  }

  addSubcommand(add)
  addSubcommand(list)
  addSubcommand(complete)
  addSubcommand(delete)

  verify()
}

object Main extends App {
  val conf = new Conf(args)

  val fileExists = Files.exists(Paths.get("./todo.json"))
  // // TODO: Replace `List` with a type that provides better by-index access
  var todos: List[TodoItem] = List()
  //
  if (fileExists) todos = read[List[TodoItem]](Source.fromFile("./todo.json").mkString)

  conf.subcommand match {
    case Some(conf.add) => {
      val writer = new PrintWriter(new File("todo.json"))
      val item   = TodoItem(false, conf.add.item())

      todos = todos :+ item

      val json = write(todos)

      writer.write(json)
      writer.close()

    }
    case Some(conf.list) => {
      todos.zipWithIndex.foreach({
        case (task, index) => {
          val status = if (task.completed) "completed" else "pending"
          println(s"[${index + 1}] Task: \"${task.action}\" Status: $status")
        }
      })
    }
    case Some(conf.complete) => {
      val index = conf.complete.index()

      if (index > todos.length) {
        println(s"Task index out of boundaries")
        sys.exit(-1)
      }

      val todo = todos(index - 1)
      todo.completed = true

      val writer = new PrintWriter(new File("todo.json"))
      val json   = write(todos)

      writer.write(json)
      writer.close()

    }
    case Some(conf.delete) => {
      val index = conf.delete.index()

      if (index > todos.length) {
        println(s"Task index out of boundaries")
        sys.exit(-1)
      }

      todos = todos.zipWithIndex
        .filterNot({ case (task, i) => index == i })
        .map({ case (task, index) => task })

      val writer = new PrintWriter(new File("todo.json"))
      val json   = write(todos)

      writer.write(json)
      writer.close()
    }
    case _ => println("Unknown mode")
  }
}
