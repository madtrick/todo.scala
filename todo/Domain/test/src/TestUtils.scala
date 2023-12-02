package main.scala

import scala.util.Random

object TestUtils {
  def randomString = Random.alphanumeric.filter(_.isLetter).take(16).mkString
  def todoFileName = s"test-file-todocollection-${randomString}"
}
