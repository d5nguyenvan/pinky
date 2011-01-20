package org.pinky.example.servlets

import org.pinky.comet.CometServlet
import javax.servlet.http.HttpServletRequest
import com.google.inject._
import java.net.URL
import scala.io.Source
import org.eclipse.jetty.continuation.Continuation
import scala.xml._
import akka.actor.Actor
import org.pinky.actor.{ActorCometClient, Resume}

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

  class MyActor extends DefaultActor  {
    override def receive = super.receive orElse handler {
      case "readfeed" => {
        for (line <- Source.fromInputStream(url.openStream).getLines) {
          val stuff = for (title <- (XML.loadString((line)) \\ "title")) yield title.text
          writer(continuation).println(stuff.mkString("<br>"))
        }
      }
    }
  }

  override val thisRef = Actor.actorOf(new MyActor)

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