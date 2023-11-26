package main.scala 

import java.nio.file.Paths
import java.nio.file.Files
import scala.io.Source

import upickle.default.{read, write}
import java.io.PrintWriter
import java.io.File


object TodoItems {
  private val filename = "todo.json"
  private val path     = Paths.get(s"./$filename")

  def load(): List[TodoItem] = {
    val fileExists = Files.exists(path)

    // TODO: Replace `List` with a type that provides better by-index access
    var todos: List[TodoItem] = List()

    if (fileExists)
      todos = read[List[TodoItem]](Source.fromFile("./todo.json").mkString)

    todos

  }

  def save(items: List[TodoItem]): Unit = {
    val writer = new PrintWriter(new File(path.toString()))
    val json   = write(items)

    writer.write(json)
    writer.close()
  }
}
