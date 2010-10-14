package org.pinky.example.servlets


import com.google.inject._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import se.scalablesolutions.akka.actor.{ActorRef, Actor}
import org.pinky.core.{PinkyServlet, ActorClient, Dispatch}



/**
 * A regular controller(serlvet) example
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

trait CakeExampleComponent {
  val example: InnerCake
  trait InnerCake {
    def saySomething: Map[String,String] 
  } 
}

trait CakeExampleContainer extends CakeExampleComponent{
  class Eater extends InnerCake {
    def saySomething = Map("name" -> "peter111" ) 
  }
}

trait ExampleServletCakeContainer {
  this: CakeExampleComponent =>

  class ExampleServletCake extends PinkyServlet {
    GET {
      (request: HttpServletRequest, response: HttpServletResponse) =>
        example.saySomething
    }
  }
}
