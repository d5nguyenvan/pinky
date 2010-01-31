package org.pinky.controlstructure

import scala.collection.jcl._
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.pinky.example.servlets._
import java.util.concurrent.{TimeUnit, CountDownLatch}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.pinky.code.util.LatchSupport._
import org.mockito.Mockito._
import se.scalablesolutions.akka.actor.ActorRegistry

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 18, 2010
 * Time: 7:07:17 PM
 * To change this template use File | Settings | File Templates.
 */


class ActorClientTest extends Spec with ShouldMatchers {


  //prepare mocks
  trait CountDownActors extends Workers {
    this: ActorClient=>
    private val pongActor = new PongActor with CountDown
    private val pingActor = new PingActor(pongActor) with CountDown
    workers=Array(pingActor,pongActor)
  }


  class DispatchMock extends Dispatch {
    def call(request: HttpServletRequest, response: HttpServletResponse)(block: => Map[String, AnyRef]) {
      block
    }
  }

  describe("an ActorClient") {
  
    it("should run actors sucessfully") {
      latch = new CountDownLatch(2)
      var request = mock(classOf[HttpServletRequest])
      var response = mock(classOf[HttpServletResponse])
      when(request.getParameter("name")).thenReturn("Jonas")
      var dispatch = new DispatchMock
      val servlet = new ExampleServlet(dispatch, new PingPongClient with CountDownActors) 
      servlet.doGet(request, response)
      val received = latch.await(4, TimeUnit.SECONDS)
      received should be(true)
      ActorRegistry.shutdownAll
    }
    it("should receive only one message if actors coming in wrong order") {
         latch = new CountDownLatch(1)
         val data = new HashMap[String, AnyRef]
         data += "name" -> "Jonas"
         val pingActor = new PongActor with CountDown
         val pongActor = new PingActor(pingActor)  with CountDown
         val client = new PingPongClient
         client.workers=Array(pingActor,pongActor)
         client.fireStarter(data)
         val received = latch.await(4, TimeUnit.SECONDS)
         received should be(true)
         ActorRegistry.shutdownAll
       }


  }
}