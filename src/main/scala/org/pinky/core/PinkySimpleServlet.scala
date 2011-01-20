package org.pinky.core

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import java.io.InputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: 12/13/10
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */

class PinkySimpleServlet extends HttpServlet {

  type Html = String
  type XHtml = xml.Elem

  private def makeCall(method: String, request: HttpServletRequest, response: HttpServletResponse) {

      if (handlers.contains(method) == false) throw new RuntimeException("could not find a handler with the request method:"+method+" in:"+handlers.toString)

      val (contenttype, content) = handlers(method)(request, response)
      response.setContentType(contenttype)
      //handle streams differently
      content match {
        case in: InputStream => {
          val out = response.getOutputStream
          val bytes = new Array[Byte](2000)
          var bytesRead = 0
          try {
            while ( {
              bytesRead = in.read(bytes);
              bytesRead
            } != -1) out.write(bytes, 0, bytesRead)
          } finally {
            in.close()
            out.close()
          }
        }
        case image: BufferedImage => {
          val out = response.getOutputStream
          ImageIO.write(image, "JPG", out)
          out.close()
        }
        case _ => {
          val writer = response.getWriter
          writer.append(content.toString)
          writer.flush()
          writer.close()
        }
      }

  }

  protected val handlers = collection.mutable.Map[String, Function2[HttpServletRequest, HttpServletResponse, Tuple2[String, Any]]]()


  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    makeCall(request.getMethod, request, response)
  }

  def GET(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, Any]) {
    handlers += (RequestMethods.GET.toString -> block)
  }

  def POST(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, Any]) {
    handlers += (RequestMethods.POST.toString -> block)
  }

  def PUT(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, Any]) {
    handlers += (RequestMethods.PUT.toString -> block)
  }

  def DELETE(block: (HttpServletRequest, HttpServletResponse) => Tuple2[String, Any]) {
    handlers += (RequestMethods.DELETE.toString -> block)
  }

}

