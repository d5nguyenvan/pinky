/*
 * MockServlet.scala
 *
 */

package org.pinky.guice

import com.google.inject.Inject
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

import _root_.scala.collection.jcl.HashMap
import org.pinky.controlstructure.Dispatch

@Singleton
class MockServlet @Inject() (dispatch:Dispatch) extends HttpServlet {

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
