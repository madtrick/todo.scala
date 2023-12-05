package main.scala.com.github.madtrick

import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.Server
import main.scala.TodoItemsCollection
import com.linecorp.armeria.server.RouteBuilder
import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.server.AbstractHttpService
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.common.HttpRequest
import main.cases.AddTodo
import com.linecorp.armeria.common.HttpStatus

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

  /** This code is commented because it behaves as catch all for /todos
    */
  // builder.service(
  //   "/todos",
  //   (ctx, req) => {
  //     val todos = TodoItemsCollection.load()
  //     println("Received request")
  //     HttpResponse.ofJson(todos)
  //   }
  // )

  // This service implentation was abandoned because I couldn't understand
  // how to get the "CompletableFuture" to work. After calling "join" the
  // handler will stuck and make no progress
  builder.service(
    "/todos_abandoned",
    new AbstractHttpService() {
      protected override def doPost(ctx: ServiceRequestContext, req: HttpRequest): HttpResponse = {
        println("POST received")
        val completable = req.aggregate()
        val request     = completable.join()
        // Code below doesn't run
        println("Headers", request.headers())
        println("Body", request.content())
        HttpResponse.of(HttpStatus.OK)
      }
    }
  )

  val server = builder.build()
  val future = server.start()
  future.join()
}
