package org.pinky.example


import org.pinky.core.ActorClient
import servlets._
import org.pinky.comet.CometServlet
import org.eclipse.jetty.continuation.ContinuationFilter
import com.google.inject.{Scopes, AbstractModule}
import org.pinky.guice.{AsServlet,AsFilter,CakeServletModule, ScalaServletModule, ScalaModule, PinkyServletContextListener, RepresentationModule}


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
      override def configureServlets {
        bindFilter[ExampleFilter].toUrl("/hello/*")
        bindFilter[ContinuationFilter].toUrl("/comet*") 
        bindServlet[ExampleCometServlet].toUrl("/comet*")
        bindServlet[ExampleRssServlet].toUrl("*.rss") 
        //bindServlet[ExampleServlet].toUrl("/hello/*")
      }
    },
    new CakeServletModule with CakeExampleContainer with ExampleServletCakeContainer {
      val example = new Eater
      override def configureServlets {
        bindServlet(new ExampleServletCake).toUrl("/hello/*")
      }  
    }


    )

}


