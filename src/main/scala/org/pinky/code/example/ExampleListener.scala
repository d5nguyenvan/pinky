package org.pinky.code.example


import com.google.inject.servlet.ServletModule
import org.pinky.code.extension.controlstructure.{ActorClient, RepresentationModule, PinkyServletContextListener}
import servlets._
import org.pinky.code.extension.comet.CometServlet
import org.eclipse.jetty.continuation.ContinuationFilter
import com.google.inject.{Scopes, AbstractModule}

/**
 * Listener example which demonstrates how to configure guice managed filters, servlets and other components the "pinky way"
 *
 * @author peter hausel gmail com (Peter Hausel)
 *
 */

class ExampleListener extends PinkyServletContextListener
{
  modules = Array(
    new RepresentationModule(),
    new AbstractModule() {
      def configure() = {
        bind(classOf[ActorClient]).to(classOf[PingPongClient])
        bind(classOf[ContinuationFilter]).in(Scopes.SINGLETON)
      }
    },
    new ServletModule() {
      filter("/hello/*").through(classOf[ExampleFilter])
      filter("/comet*").through(classOf[ContinuationFilter])
      serve("/comet*").by(classOf[ExampleCometServlet])
      serve("*.rss").by(classOf[ExampleRssServlet])
      serve("/hello/*").by(classOf[ExampleServlet])
    }

    )

}


