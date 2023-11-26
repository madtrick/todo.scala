package main.scala

import org.rogach.scallop._

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
  val conf  = new Conf(args)
  var todos = TodoItems.load()

  conf.subcommand match {
    case Some(conf.add) => {
      val item = TodoItem(false, conf.add.item())

      todos = todos :+ item

      TodoItems.save(todos)

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

      TodoItems.save(todos)
    }
    case Some(conf.delete) => {
      val index = conf.delete.index()

      if (index > todos.length) {
        println(s"Task index out of boundaries")
        sys.exit(-1)
      }

      todos = todos.zipWithIndex
        .filterNot({ case (task, i) => (index - 1) == i })
        .map({ case (task, index) => task })

      TodoItems.save(todos)
    }
    case _ => println("Unknown mode")
  }
}
