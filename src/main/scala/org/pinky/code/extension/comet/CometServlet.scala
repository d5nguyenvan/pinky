package org.pinky.code.extension.comet

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.atmosphere.util.XSSHtmlFilter
import java.util.LinkedList
import org.pinky.code.extension.controlstructure.ActorCometClient
import se.scalablesolutions.akka.actor.Actor
import reflect.Manifest
import org.atmosphere.cpr.{AtmosphereServlet, Meteor, BroadcastFilter}
import org.atmosphere.handler.ReflectorServletProcessor
import javax.servlet.{ServletException, ServletConfig}

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 20, 2010
 * Time: 11:26:16 PM
 * To change this template use File | Settings | File Templates.
 */

/*
class PingPongClient(m:Meteor,req:HttpServletRequest,res:HttpServletResponse,workers:_*) extends ActorCometClient(m,req,res,workers:_*) {
  fireStarter {
    workers(0) ! reqData("name")
  }
}
* class MyComet extends CometServlet {
*     class MyActorCometClient extends ActorCometClient {
*       def receive = handler {
             case "test" =>
        }
        def callback = {
            this!! "test"
        }
*     }
*
*    type T= MyActorCometClient
*    }
* }
* */
abstract class CometServlet extends  HttpServlet{
  type T <: ActorCometClient
  var timeout = -1
  var workers:Seq[Actor] = _

  private val list = new LinkedList[BroadcastFilter]
  list.add(new XSSHtmlFilter)

  override def service(request: HttpServletRequest, response: HttpServletResponse) = {
    val meteor = Meteor.build(request, list, null);
    response.setContentType("text/html;charset=ISO-8859-1")
    response.addHeader("Cache-Control", "private")
    response.addHeader("Pragma", "no-cache")
    meteor.suspend(timeout)
    val cometClient = createInstanceOfActorCometClient[T](meteor, request, response, workers)
    cometClient.start
    cometClient.fireStarter(cometClient.callback)
  }

  private def createInstanceOfActorCometClient[T <: ActorCometClient](meteor: Meteor, request: HttpServletRequest, response: HttpServletResponse, actors: Seq[Actor])(implicit m: Manifest[T]):ActorCometClient = {
    try {
      val constructor = m.erasure.getConstructors()(0)
      constructor.newInstance(meteor, request, response, actors).asInstanceOf[ActorCometClient]
    } catch {
      case e: Exception => {
        println("error creating Actor client. params:" + meteor, request, response, actors.toArray + " type:" + m.erasure.toString + "\n\n")
        e.printStackTrace
        throw new Exception
      }
    }
  }
     
}
