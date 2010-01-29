package org.pinky.code.extension.comet

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import java.util.LinkedList
import org.pinky.code.extension.controlstructure.ActorCometClient
import se.scalablesolutions.akka.actor.Actor
import reflect.Manifest
import javax.servlet.{ServletException, ServletConfig}
import org.eclipse.jetty.continuation.{ContinuationSupport, Continuation}
import java.lang.reflect.ParameterizedType


/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 20, 2010
 * Time: 11:26:16 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class CometServlet[T <: ActorCometClient] extends HttpServlet {
    private def clazz = {
      val superType = getClass.getGenericSuperclass.asInstanceOf[ParameterizedType]
      superType.getActualTypeArguments()(0).asInstanceOf[Class[_]]
    }

  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    if (clazz == null) {
      throw new Exception("you need to register an ActorCometClient first")
    } else {
      response.setContentType("text/html;charset=ISO-8859-1")
      response.addHeader("Cache-Control", "private")
      response.addHeader("Pragma", "no-cache")
      val continuation = ContinuationSupport.getContinuation(request)
      val out = response.getWriter
      if (continuation.isExpired) {
        response.setStatus(500)
        out.println("timeout reached, response is returned...\n")
        out.flush()
        out.close()
      }
      if (continuation.isInitial) {
        continuation.suspend(response)
        out.flush()
        val cometClient = createInstanceOfActorCometClient(continuation, request)
        cometClient.fireStarter(cometClient.callback)
      }
    }
  }


  private def createInstanceOfActorCometClient(continuation: Continuation, request: HttpServletRequest): ActorCometClient = {
    try {
      val constructor = clazz.getConstructors()(0)
      constructor.newInstance(continuation, request).asInstanceOf[ActorCometClient]
    } catch {
      case e: Exception => {
        e.printStackTrace
        continuation.resume()
        throw new Exception
      }
    }
  }

}
