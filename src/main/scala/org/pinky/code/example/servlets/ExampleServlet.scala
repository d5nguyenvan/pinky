package org.pinky.code.example.servlets

import scala.collection.jcl.HashMap
import scala.collection.jcl.Map
import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import org.pinky.code.extension.controlstructure.Dispatch
import se.scalablesolutions.akka.actor.Actor
import org.pinky.code.extension.controlstructure.{ActorClient, ActorDispatch, Dispatch}

trait PingActor extends Actor {
  def receive = {
    case "Jonas" => {
      PongActor ! "whatsnext"
    }
    case "yay" => println("replay from Pong")
    case _ => println("received unknown message")
  }
}
object PingActor extends PingActor

trait PongActor extends Actor {
  def receive = {
    case "whatsnext" => {println("pong")
      reply("yay")
    }
  }
}
object PongActor extends PongActor

class PingPongClient(reqData:Map[String,AnyRef],actors:Actor*) extends ActorClient(reqData,actors:_*) {
  fireStarter {
    PingActor ! reqData("name")
  }
}

/**
 * A regular controller(serlvet) example
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

@Singleton
class ExampleServlet @Inject() (dispatch:Dispatch) extends HttpServlet with ActorDispatch  {

  override def doGet(req: HttpServletRequest,
                    res: HttpServletResponse) =
    {
      dispatch.call(req, res){
        val data = new HashMap[String, AnyRef]
        //val params = captureIn(req,'name,'id)
        data += "name" -> "Jonas"
        launch[PingPongClient] using (data,PingActor,PongActor)
      }

    }
  override def doPost (req: HttpServletRequest,
                    res: HttpServletResponse) =
    {
      dispatch.call(req, res){
        val data = new HashMap[String, AnyRef]
        data += "message" -> "Changing state with POST"
        data
      }

    }

}
