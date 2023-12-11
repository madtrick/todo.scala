import mill._, scalalib._

object Cli extends ScalaModule {
  def scalaVersion = "2.13.12"

  def ivyDeps = Agg(
    ivy"org.rogach::scallop:5.0.1"
  )

  def moduleDeps = Seq(Domain)
}

object Domain extends ScalaModule {
  def scalaVersion = "2.13.12"

  def ivyDeps = Agg(
    ivy"com.lihaoyi::upickle:3.1.3"
  )

  object test extends ScalaTests {
    def ivyDeps       = Agg(ivy"com.lihaoyi::utest:0.7.11")
    def testFramework = "utest.runner.Framework"
  }
}

object WebServer extends ScalaModule {
  def scalaVersion = "2.13.12"

  def moduleDeps = Seq(Cli)

  def ivyDeps = Agg(
    ivy"com.linecorp.armeria::armeria-scala:1.26.3",
    ivy"org.slf4j:slf4j-simple:2.0.9"
  )
}
