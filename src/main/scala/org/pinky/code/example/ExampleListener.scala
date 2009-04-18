package org.pinky.code.example


import com.google.inject.servlet.ServletModule
import extension._
import extension.controlstructure._
import com.google.inject.servlet.{ServletModule, GuiceServletContextListener}
import com.google.inject.{AbstractModule, Guice, Injector, Module}
import servlets.{ExampleFilter, ExampleRssServlet, ExampleServlet}

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
      new ServletModule(){

        // Imports the DSL fixes for scala
        // Note how we can import it in 
        //    the precise scope it's gonna be used
        import org.pinky.code.extension.guice.DSLFixes._

        filter("/hello/*").through(classOf[ExampleFilter])
        serve("*.rss").by(classOf[ExampleRssServlet])
        serve("/hello/*").by(classOf[ExampleServlet])
      }

    )

}

