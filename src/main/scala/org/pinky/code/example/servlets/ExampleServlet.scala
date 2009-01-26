package org.pinky.code.example.servlets

import _root_.scala.collection.jcl.HashMap
import com.google.inject.Inject
import extension.controlstructure.BaseControl
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 * A regular controller(serlvet) example
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

@Singleton
class ExampleServlet @Inject() (dispatch:BaseControl) extends HttpServlet {

  override def doGet(req: HttpServletRequest,
                    res: HttpServletResponse) =
    {
      dispatch.call(req, res){
        val data = new HashMap[String, AnyRef]
        data += "message" -> "Hello World"
        data
      }

    }
  override def doPost (req: HttpServletRequest,
                    res: HttpServletResponse) =
    {
      dispatch.call(req, res){
        val data = new HashMap[String, AnyRef]
        data += "message" -> "Changing state with POST"
        data
      }

    }

}
