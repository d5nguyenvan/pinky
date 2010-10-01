package org.pinky.example


import com.google.inject.servlet.ServletModule
import org.pinky.core.ActorClient
import servlets._
import org.pinky.comet.CometServlet
import org.eclipse.jetty.continuation.ContinuationFilter
import com.google.inject.{Scopes, AbstractModule}
import org.pinky.guice.{ScalaServletModule, PinkyServletContextListener, RepresentationModule}
import uk.me.lings.scalaguice.ScalaModule
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
    new AbstractModule with ScalaModule{
      def configure {
        bind[ActorClient].to[PingPongClient] 
        bind[ContinuationFilter].in(Scopes.SINGLETON)
      }
    },
    new ServletModule with ScalaServletModule {
      filterThrough[ExampleFilter] ("/hello/*")
      filterThrough[ContinuationFilter] ("/comet*") 
      serveWith[ExampleCometServlet] ("/comet*")
      serveWith[ExampleRssServlet] ("*.rss") 
      serveWith[ExampleServlet] ("/hello/*")
    }

    )

}


