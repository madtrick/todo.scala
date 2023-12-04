package main.scala

import org.rogach.scallop._
import main.cases._

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
  var todos = TodoItemsCollection.load()

  conf.subcommand match {
    case Some(conf.add) => {
      AddTodo(conf.add.item(), TodoItemsCollection)
    }
    case Some(conf.list) => {
      ListTodos(TodoItemsCollection)
    }
    case Some(conf.complete) => {
      val index = conf.complete.index()

      CompleteTodo(index, TodoItemsCollection) match {
        case Left(value)  => println(s"Invalid index \"$index\"")
        case Right(value) => ()
      }
    }
    case Some(conf.delete) => {
      val index = conf.delete.index()

      DeleteTodo(index, TodoItemsCollection) match {
        case Left(value)  => println(s"Invalid index \"$index\"")
        case Right(value) => ()
      }
    }
    case _ => println("Unknown mode")
  }
}
