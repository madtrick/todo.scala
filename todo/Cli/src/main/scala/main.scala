package main.scala

import org.rogach.scallop._
import main.cases._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  object add extends Subcommand("add") {
    val item = trailArg[String]()
  }

  object list extends Subcommand("list") {}

  object complete extends Subcommand("complete") {
    val id = opt[Int](required = true)
  }

  object delete extends Subcommand("delete") {
    val id = opt[Int](required = true)
  }

  addSubcommand(add)
  addSubcommand(list)
  addSubcommand(complete)
  addSubcommand(delete)

  verify()
}

object Main extends App {
  val conf  = new Conf(args)
  var todos = TodoItemsCollection.load()

  conf.subcommand match {
    case Some(conf.add) => {
      AddTodo(conf.add.item(), TodoItemsCollection)
    }
    case Some(conf.list) => {
      val todos = TodoItemsCollection.load

      todos
        .foreach({
          case (todo) => {
            val status    = if (todo.completed) "completed" else "pending"
            val createdAt = todo.createdAt.toString("d MMMM, yyyy")
            println(
              s"[${todo.id}] Task: \"${todo.action}\". Status: $status. Created at: \"$createdAt\""
            )
          }
        })

    }
    case Some(conf.complete) => {
      val id = conf.complete.id()

      CompleteTodo(id, TodoItemsCollection) match {
        case Left(value)  => println(s"Invalid id \"$id\"")
        case Right(value) => ()
      }
    }
    case Some(conf.delete) => {
      val id = conf.delete.id()

      DeleteTodo(id, TodoItemsCollection) match {
        case Left(value)  => println(s"Invalid index \"$id\"")
        case Right(value) => ()
      }
    }
    case _ => println("Unknown mode")
  }
}
