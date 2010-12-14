package org.pinky.core

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: 12/13/10
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */

class PinkySimpleServlet extends HttpServlet {

  def makeCall(method: String, request: HttpServletRequest, response: HttpServletResponse) {
    try {
      val (contenttype, content) =  handlers(method)(request, response)
      response.setContentType(contenttype)
      val writer = response.getWriter
      writer.append(content.toString)
      writer.flush()
      writer.close()
    } catch {
      case ex: NoSuchElementException => throw new RuntimeException("could not find a handler for this request method, you'll need to implement a call to " + request.getMethod)
    }
  }

  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    makeCall(request.getMethod, request, response)
  }

  private val handlers = collection.mutable.Map[String, Function2[HttpServletRequest, HttpServletResponse, Tuple2[String, xml.Elem]]]()

  def GET(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, xml.Elem]) {
    handlers += (RequestMethods.GET.toString -> block)
  }

  def POST(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, xml.Elem]) {
    handlers += (RequestMethods.POST.toString -> block)
  }

  def PUT(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, xml.Elem]) {
    handlers += (RequestMethods.PUT.toString -> block)
  }

  def DELETE(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, xml.Elem]) {
    handlers += (RequestMethods.DELETE.toString -> block)
  }

}

