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
      val node   = TodoItemRequestConverter.mapper.readTree(request.contentUtf8())
      val action = TodoItemRequestConverter.stringValue(node, "action")

      return new TodoItem(false, action)
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
}