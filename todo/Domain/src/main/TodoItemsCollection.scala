package main.scala

import java.nio.file.Paths
import java.nio.file.Files
import scala.io.Source

import upickle.default.{read, write}
import java.io.PrintWriter
import java.io.File
import java.nio.file.Path

trait TodoItemsCollectionTrait {
  def load(): List[TodoItem]
  def save(items: List[TodoItem]): Unit
  def filepath_=(path: Path): Unit
  def filepath: Path
}

// TODO: rename this object to FileTodoItemsCollection
object TodoItemsCollection extends TodoItemsCollectionTrait {
  private val defaultFilename: String = "todo.json"
  private var _filepath: Path         = Paths.get(s"./$defaultFilename")

  /** Note that for the "setter" to work as an assignment (i.e. "".filepath = Path" instead of
    * ".filepath_=(Path)") the "filepath" getter has to also be defined
    *
    * Discovered that here
    * https://users.scala-lang.org/t/how-does-scala-recognize-as-space-in-the-setter-construct-while-is-allowed-being-the-part-of-a-function-property-name/7251/8
    */
  def filepath_=(path: Path): Unit = {
    _filepath = path
  }

  def filepath: Path = _filepath

  def load(): List[TodoItem] = {
    val fileExists = Files.exists(_filepath)

    // TODO: Replace `List` with a type that provides better by-index access
    var todos: List[TodoItem] = List()

    if (fileExists)
      todos = read[List[TodoItem]](Source.fromFile(_filepath.toAbsolutePath().toString()).mkString)

    todos
  }

  def save(items: List[TodoItem]): Unit = {
    val writer = new PrintWriter(new File(_filepath.toString()))
    val json   = write(items)

    writer.write(json)
    writer.close()
  }

  def length: Int = {
    val todos = this.load

    return todos.length
  }
}
