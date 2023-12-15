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
import com.linecorp.armeria.server.logging.LoggingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.linecorp.armeria.server.annotation.JacksonRequestConverterFunction
import com.linecorp.armeria.server.annotation.JacksonResponseConverterFunction
import java.text.SimpleDateFormat

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

  val mapper = new ObjectMapper();
  mapper.registerModule(new JodaModule());

  /** NOTE: after doing this, dates are still returned as timestamps
    *
    * For example:
    *
    * [{"id":0,"completed":false,"action":"vacuum clean the kitchen","createdAt":1702577897882}]
    *
    * Related links
    *
    *   - https://stackoverflow.com/questions/27978762/jackson-serializationfeature-write-dates-as-timestamps-not-turning-off-timestamp
    *   - https://fasterxml.github.io/jackson-databind/javadoc/2.6/com/fasterxml/jackson/databind/SerializationFeature.html#WRITE_DATES_AS_TIMESTAMPS
    *
    * Maybe I'm missing specifying what's the format I want the dates serialized to.
    */
  mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  builder.annotatedService(
    new Object() {
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
    },
    new JacksonRequestConverterFunction(mapper),
    new JacksonResponseConverterFunction(mapper)
  )

  builder.decorator(LoggingService.newDecorator())

  val server = builder.build()
  val future = server.start()
  future.join()
}
