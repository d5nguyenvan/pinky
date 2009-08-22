package org.pinky.code.extension.controlstructure

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.google.inject._
import java.io.{BufferedWriter, OutputStreamWriter, PrintWriter, StringWriter}
import scala.collection.jcl._
import org.pinky.code.extension.Representations


/**
 * for the following call
 * <pre>
 *  dispatch.call(req, res){
 *       val data = new HashMap[String, AnyRef]
 *       data += "message" -> "Hello World"
 *       data
 * }
 * </pre>
 * this class tries to figure out the representation based on the URL's extension (if there is no extension,
 * then html will be used), besides this, for html rendering the template will be determined based on the url too
 * (unless it's explicitly set in the user's data with a "template" key).
 * In case of error, a 500 response will be sent (with the exception)
 *
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
@Singleton
  class  DefaultControl @Inject() (representation:Representations) extends BaseControl {

  /**
   * @param request
   * @param response
   * @param block this is the user provided block that needs to be executed. the return value of this block will be used
   * as user data
   * this method executes the block coming from the user, sets the appropriate content type based on extension
   * then calls the appropriate representation. if error occurrs, a 500 will be sent back to the user with the exception 
   */
  def call(request: HttpServletRequest, response: HttpServletResponse)(block: => Map[String, AnyRef]) {
    try {
      val data = block
      val uri = getUri(request)
      val format = if (uri.lastIndexOf(".") != -1) uri.substring(uri.lastIndexOf(".") + 1, uri.length) else "html"
      if  (format == "html")  data.get("template") match {case None => data += "template" -> uri; case _ => {}}
      route(data, uri, format, response)
    } catch {
      case e: Exception => {
        response.setStatus(500)
        val out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream))
        out.write(errorResponse(printEx(e)))
        out.close
      }
    }


  }
  /**
  *  @param request
  *  @return request uri without context path
  *
  */
  private def getUri(request: HttpServletRequest):String = {request.getRequestURI.replaceFirst(request.getContextPath,"")}
 /**
  * @param data user data
  * @param uri  incoming request URI
  * @param response response is needed for content type 
  */
  protected def route(data: Map[String, AnyRef], uri: String, ext: String, response: HttpServletResponse) {
    response.setContentType(representation.contentType(ext))
    representation.mode(ext).write(data, response.getOutputStream)
  }
  /**
   * This method provides the template for error messages
  *  @param error  takes the error message
  *
  */
  private def errorResponse(error: String) = {
    val ret =
    <html>
    <body>
    <div>
    {error}
    </div>
    </body>
    </html>;
    ret.toString
  }

 /**
  * This method provides the template for error messages
  *  @param t the exception which needs to be printed in the response 
  *
  */
  private def printEx(t: Throwable): String =
    {
      val sw = new StringWriter();
      val pw = new PrintWriter(sw, true);
      t.printStackTrace(pw);
      pw.flush();
      sw.flush();
      return sw.toString();
    }

}
