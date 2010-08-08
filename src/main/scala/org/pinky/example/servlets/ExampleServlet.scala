package org.pinky.example.servlets


import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import se.scalablesolutions.akka.actor.{ActorRef, Actor}
import org.pinky.core.{PinkyServlet, ActorClient, Dispatch}

class PingActor(pong: ActorRef) extends Actor {
  def receive = {
    case "Jonas" => {
      pong ! "whatsnext"
    }
    case "yay" => {
      println("replay from Pong")
    }
    case _ => println("Ping:unknown message was received")
  }
}
class PongActor extends Actor {
  def receive = {
    case "whatsnext" => {
      println("pong")
      reply("yay")
    }
    case _ => println("Pong:unknown message was received")
  }
}

trait Workers {
  this: ActorClient =>
  private val pongActor = Actor.actorOf(new PongActor)
  workers = Array(Actor.actorOf(new PingActor(pongActor)), pongActor)
}

class PingPongClient extends ActorClient with Workers {
  def callback(reqData: Map[String, AnyRef]) {
    workers(0) ! reqData("name")
  }
}


/**
 * A regular controller(serlvet) example
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

@Singleton
class ExampleServlet @Inject()(dispatch: Dispatch, actorClient: ActorClient) extends PinkyServlet {
  
  GET {
    (request: HttpServletRequest, response: HttpServletResponse) =>
      val data = Map("name" -> {if (request.getParameter("name") == null) "default" else request.getParameter("name")})
      actorClient.fireStarter(data)
  }

  POST {
    (request: HttpServletRequest, response: HttpServletResponse) =>
        Map("name" -> "Changing state with POST")
  }

}
