package org.pinky.code.example.servlets

import org.pinky.code.extension.comet.CometServlet
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.atmosphere.cpr.Meteor
import com.google.inject._
import java.net.URL
import scala.io.Source.fromInputStream
import org.pinky.code.extension.controlstructure.{Resume, ActorCometClient}

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 24, 2010
 * Time: 10:15:31 PM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
class ExampleCometServlet extends CometServlet {
  val url = new URL("http://twitter.com/statuses/user_timeline/5047741.rss")
  class MyActorCometClient(meteor: Meteor, request: HttpServletRequest, response: HttpServletResponse)
            extends ActorCometClient(meteor,request,response) {
    override def receive = handler {
      case "callfeed" => meteor.broadcast(fromInputStream(url.openStream).getLines.mkString)
    }
    def callback = {
      this ! "callfeed"
      Thread.sleep(5000)
      this ! Resume
    }
  }
  type T = MyActorCometClient
}