package org.pinky.example.servlets

import org.pinky.comet.CometServlet
import javax.servlet.http.HttpServletRequest
import com.google.inject._
import java.net.URL
import scala.io.Source
import org.eclipse.jetty.continuation.Continuation
import scala.xml._
import org.pinky.util.ARM.using
import org.pinky.actor.Resume
import org.pinky.core.ActorCometClient

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 24, 2010
 * Time: 10:15:31 PM
 * To change this template use File | Settings | File Templates.
 */

class MyActorCometClient(continuation: Continuation, request: HttpServletRequest)
        extends ActorCometClient(continuation, request) {
  val url = new URL("http://twitter.com/statuses/user_timeline/5047741.rss")

  override def receive = handler {
    case "readfeed" => {
      for (line <- Source.fromInputStream(url.openStream).getLines) {
        val stuff = for (title <- (XML.loadString((line)) \\ "title")) yield title.text
        writer(continuation).println(stuff.mkString("<br>"))
      }
    }
  }

  def callback = {
    println("about to hit callback")
    Thread.sleep(1000)
    thisRef ! "readfeed"
    println("about to hit second sleep")
    Thread.sleep(1000)
    thisRef ! Resume
  }
}
@Singleton class ExampleCometServlet extends CometServlet[MyActorCometClient]