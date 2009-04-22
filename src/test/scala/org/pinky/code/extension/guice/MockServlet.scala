/*
 * MockServlet.scala
 *
 */

package org.pinky.code.extension.controlstructure

import com.google.inject.Inject
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

import _root_.scala.collection.jcl.HashMap

@Singleton
class MockServlet @Inject() (dispatch:BaseControl) extends HttpServlet {

    override def doGet(req: HttpServletRequest,
                     res: HttpServletResponse) =
  {
    dispatch.call(req, res){
      val data = new HashMap[String, AnyRef]
      data += "message" -> "Hello World"
      data
    }

  }

}
