package org.pinky.code.example


import com.google.inject.servlet.ServletModule
import servlets.{ExampleFilter, ExampleRssServlet, ExampleServlet}
import org.pinky.code.extension.controlstructure.{RepresentationModule, PinkyServletContextListener}

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
    new ServletModule() {

      // Imports the DSL fixes for scala
      // Note how we can import it in
      //    the precise scope it's gonna be used
      filter("/hello/*").through(classOf[ExampleFilter])
      serve("*.rss").by(classOf[ExampleRssServlet])
      serve("/hello/*").by(classOf[ExampleServlet])
    }

    )

}


