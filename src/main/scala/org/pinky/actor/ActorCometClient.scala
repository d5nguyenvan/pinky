package org.pinky.core

import javax.servlet.http.HttpServletRequest
import java.io.PrintWriter
import org.eclipse.jetty.continuation.Continuation
import org.pinky.actor.{Resume, Client}

abstract class ActorCometClient(continuation: Continuation, request: HttpServletRequest) extends Client {
  if (workers != null) for (actorRef <- workers if !actorRef.isRunning) thisRef.startLink(actorRef)

  def callback

  def handler(messageHandler: PartialFunction[Any, Unit]): PartialFunction[Any, Unit] = {
    val resumeHandler: PartialFunction[Any, Unit] = {
      case Resume => {
        val out = continuation.getServletResponse.getWriter
        out.flush()
        out.close()
        thisRef.shutdownLinkedActors
        continuation.complete()
      }
    }
    resumeHandler orElse messageHandler 
  }

  def writer(continuation: Continuation): PrintWriter = continuation.getServletResponse.getWriter
}


