package org.pinky.code.example


import com.google.inject.servlet.ServletModule
import com.google.inject.AbstractModule
import org.pinky.code.extension.controlstructure.{ActorClient, RepresentationModule, PinkyServletContextListener}
import servlets._
import org.pinky.code.extension.comet.CometServlet

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
      }
    },
    new ServletModule() {
      filter("/hello/*").through(classOf[ExampleFilter])
      serve("*.rss").by(classOf[ExampleRssServlet])
      serve("/comet").by(classOf[CometServlet])
      serve("/hello/*").by(classOf[ExampleServlet])
    }

    )

}


