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
import com.fasterxml.jackson.databind.JsonNode
import main.cases.ListTodos
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.RequestConverter
import main.scala.com.github.madtrick.converters.TodoItemRequestConverter
import main.scala.TodoItem
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Put
import main.cases.UpdateTodo

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

  builder.annotatedService(new Object() {
    @Post("/todos")
    @RequestConverter(classOf[TodoItemRequestConverter])
    def createTodo(todo: TodoItem): Unit = {
      AddTodo(todo.action, TodoItemsCollection)
    }

    @Get("/todos")
    def listTodos(): HttpResponse = {
      HttpResponse.ofJson(TodoItemsCollection.load())
    }

    @Put("/todos/:id")
    @RequestConverter(classOf[TodoItemRequestConverter])
    def updateTodo(@Param id: Int, todo: TodoItem): HttpResponse = {
      UpdateTodo(id, todo.completed, todo.action, TodoItemsCollection)
      HttpResponse.ofJson(TodoItemsCollection.findById(id))
    }
  })

  val server = builder.build()
  val future = server.start()
  future.join()
}
