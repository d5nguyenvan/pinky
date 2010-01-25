package org.pinky.code.extension.controlstructure

import com.google.inject.AbstractModule
import com.google.inject.name.Names._
import org.pinky.code.extension._

/**
 * Provides basic representations, the easiest way to extend this if you provide your own module
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
class RepresentationModule extends AbstractModule {
  /**
   * Guice's way to provide components for dependency injection. I could not really scalafy this one,
   * so this is pretty much the same as the java thing
   */
  protected def configure {
    bind(classOf[Representation]).annotatedWith(named("html")).to(classOf[HtmlRepresentationVelocity])
    bind(classOf[Representation]).annotatedWith(named("rss")).to(classOf[RssRepresentation])
    bind(classOf[Representation]).annotatedWith(named("xml")).to(classOf[XmlRepresentation])
    bind(classOf[Representation]).annotatedWith(named("json")).to(classOf[JsonRepresentation])
    bind(classOf[Representations]).to(classOf[DefaultRepresentations])
    bind(classOf[Dispatch]).to(classOf[DefaultControl])
  }
}
