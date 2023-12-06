package main.scala.com.github.madtrick.converters

import com.linecorp.armeria.server.annotation.RequestConverterFunction
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.common.AggregatedHttpRequest
import java.lang.reflect.ParameterizedType
import main.scala.TodoItem
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.JsonNode

class TodoItemRequestConverter extends RequestConverterFunction {

  def convertRequest(
      ctx: ServiceRequestContext,
      request: AggregatedHttpRequest,
      expectedResultType: Class[_],
      expectedParameterizedResultType: ParameterizedType
  ): Object = {
    if (expectedResultType == classOf[TodoItem]) {
      val node      = TodoItemRequestConverter.mapper.readTree(request.contentUtf8())
      val action    = TodoItemRequestConverter.stringValue(node, "action")
      val completed = TodoItemRequestConverter.booleanValue(node, "completed")

      // -1 is a temporary placeholder. The converter should not
      // return TodoItems but payloads
      return new TodoItem(-1, completed, action)
    } else {
      RequestConverterFunction.fallthrough()
    }
  }
}

object TodoItemRequestConverter {
  val mapper: ObjectMapper = new ObjectMapper()

  def stringValue(jsonNode: JsonNode, field: String): String = {
    val value: JsonNode = jsonNode.get(field)

    // TODO: do not throw. Instead return an Option
    if (value == null) {
      throw new IllegalArgumentException(field + " is missing!")
    }
    return value.textValue()
  }

  def booleanValue(jsonNode: JsonNode, field: String): Boolean = {
    val value: JsonNode = jsonNode.get(field)

    // TODO: do not throw. Instead return an Option
    if (value == null) {
      throw new IllegalArgumentException(field + " is missing!")
    }
    return value.booleanValue()
  }
}
