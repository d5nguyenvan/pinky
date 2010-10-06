package org.pinky.example


import org.pinky.core.ActorClient
import servlets._
import org.pinky.comet.CometServlet
import org.eclipse.jetty.continuation.ContinuationFilter
import com.google.inject.{Scopes, AbstractModule}
import org.pinky.guice.{ScalaServletModule, ScalaModule, PinkyServletContextListener, RepresentationModule}

/**
 * Listener example which demonstrates how to configure guice managed filters, servlets and other components the "pinky way"
 *
 * @author peter hausel gmail com (Peter Hausel)
 *
 */

class ExampleListener extends PinkyServletContextListener
{
  modules = Array (
    new RepresentationModule(),
    new ScalaModule{
      def configure {
        bind[ActorClient].to[PingPongClient] 
        bind[ContinuationFilter].in(Scopes.SINGLETON)
      }
    },
    new ScalaServletModule {
      filterThrough[ExampleFilter] ("/hello/*")
      filterThrough[ContinuationFilter] ("/comet*") 
      serveWith[ExampleCometServlet] ("/comet*")
      serveWith[ExampleRssServlet] ("*.rss") 
      serveWith[ExampleServlet] ("/hello/*")
    }

    )

}


