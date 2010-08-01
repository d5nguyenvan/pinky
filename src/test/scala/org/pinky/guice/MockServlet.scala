/*
 * MockServlet.scala
 *
 */

package org.pinky.guice

import com.google.inject.Inject
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.pinky.core.Dispatch


@Singleton
class MockServlet @Inject() (dispatch:Dispatch) extends HttpServlet {

    override def doGet(req: HttpServletRequest,
                     res: HttpServletResponse) =
  {
    dispatch.call(req, res){
      Map("message" -> "Hello World")
    }

  }

}
