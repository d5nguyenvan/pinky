package org.pinky.core

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import com.google.inject.Inject

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jul 31, 2010
 * Time: 11:58:23 PM
 * To change this template use File | Settings | File Templates.
 */

class PinkyServlet extends HttpServlet {
  @Inject() val dispatch: ServletDispatch = null

  import collection.JavaConversions._

  def makeCall(method: String, request: HttpServletRequest, response: HttpServletResponse) {
      try {
      dispatch.callSuppliedBlock(request, response, handlers(method))
    } catch {
      case ex: NoSuchElementException => throw new RuntimeException("could not find a handler for this request method, you'll need to implement a call to " + request.getMethod)
      case ex: NullPointerException => throw new RuntimeException("guice cound not inject a ServletDispatch, was a class with this type registered?")
    }
  }
  
  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
       makeCall(request.getMethod,request,response)
  }

  private val handlers = collection.mutable.Map[String, Function2[HttpServletRequest, HttpServletResponse, Map[String, AnyRef]]]()

  def convert(params: Map[String, Array[String]]): Map[String, AnyRef] = {
    val map = collection.mutable.Map[String, AnyRef]()
    for ((key, value) <- params) if (value.size == 1) map += ("key" -> value(0)) else map += ("key" -> value)
    return map.toMap
  }
  //GET {
  // (req:HttpServletRequest,res:HttpServletResponse) =>
  //  val data = convert(req.getParameterMap)
  //  data+Map("foo"->"foo")   
  //}

  def GET(block: (HttpServletRequest, HttpServletResponse) => Map[String, AnyRef]) {
    handlers += ("GET" -> block)
  }

  def POST(block: (HttpServletRequest, HttpServletResponse) => Map[String, AnyRef]) {
    handlers += ("POST" -> block)
  }

  def PUT(block: (HttpServletRequest, HttpServletResponse) => Map[String, AnyRef]) {
    handlers += ("PUT" -> block)
  }

  def DELETE(block: (HttpServletRequest, HttpServletResponse) => Map[String, AnyRef]) {
    handlers += ("DELETE" -> block)
  }
}