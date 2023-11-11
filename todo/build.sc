import mill._, scalalib._

object Todo extends ScalaModule {
  def scalaVersion = "3.3.1"

  def ivyDeps = Agg(ivy"org.rogach::scallop:5.0.1", ivy"com.lihaoyi::upickle:3.1.3")

  object test extends ScalaTests {
    def ivyDeps       = Agg(ivy"com.lihaoyi::utest:0.7.11")
    def testFramework = "utest.runner.Framework"
  }
}
