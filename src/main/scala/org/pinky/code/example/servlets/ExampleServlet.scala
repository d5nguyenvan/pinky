package org.pinky.code.example.servlets

import scala.collection.jcl.HashMap
import scala.collection.jcl.Map
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import se.scalablesolutions.akka.actor.Actor
import org.pinky.code.extension.controlstructure.{ActorClient, ActorDispatch, Dispatch}

class PingActor(pong:Actor) extends Actor {
  def receive = {
    case "Jonas" => {
      pong ! "whatsnext"
    }
    case "yay" => {
      println("replay from Pong")
      stop
    }
    case _ => println("unknown message was received")
  }
}
class PongActor extends Actor {
  def receive = {
    case "whatsnext" => {
      println("pong")
      reply("yay")
      stop
    }
    case _ => println("unknown message was received")  
  }
}

trait Actors {
   val pongActor = new PongActor
   val pingActor = new PingActor(pongActor)
}

class PingPongClient(reqData: Map[String, AnyRef], actors: Actor*) extends ActorClient(reqData, actors:_*) {
  fireStarter {
    actors(0) ! reqData("name")
  }
}


/**
 * A regular controller(serlvet) example
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

@Singleton
class ExampleServlet @Inject()(dispatch: Dispatch) extends HttpServlet with ActorDispatch with Actors{

  override def doGet(req: HttpServletRequest, res: HttpServletResponse) = {
    dispatch.call(req, res) {
      val data = new HashMap[String, AnyRef]
      data += "name" -> req.getParameter("name")
      launch[PingPongClient] using (data, pingActor, pongActor)
    }
  }

  override def doPost(req: HttpServletRequest, res: HttpServletResponse) = {
    dispatch.call(req, res) {
      val data = new HashMap[String, AnyRef]
      data += "message" -> "Changing state with POST"
      data
    }

  }

}
