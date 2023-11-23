package main.scala.com.github.madtrick

import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.Server

class SampleService {
  @Post("/hello")
  def getHello: HttpResponse = {
    println("GET HELLO")
    return HttpResponse.of("Hello world")
  }
}

object Main extends App {
  val builder = Server.builder()
  builder.http(8080)

  builder.service("/hello", (ctx, req) => HttpResponse.of("Hello, world!"))

  val server = builder.build()
  val future = server.start()
  future.join()
}
