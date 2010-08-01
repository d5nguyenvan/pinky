package org.pinky.representation

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named

/**
 * Provides the default representations, pinky supports json/rss/xml/html
 * out of the box.
 *
 * <p>
 * You will need to extend both contentTypes and representationModes in case
 * of adding new representations
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

@Singleton
class DefaultRepresentations extends Representations {
  private val contentTypes = Map(
    "html" -> "text/html",
    "rss" -> "application/rss+xml",
    "xml" -> "text/xml",
    "json" -> "application/json"
    )

  private val representationModes = collection.mutable.Map[String, Representation]()

  @Inject
  def injectJsonRep(@Named("json") jsonRep: Representation) {
    representationModes += "json" -> jsonRep
  }

  @Inject
  def injecthtmlRep(@Named("html") htmlRep: Representation) {
    representationModes += "html" -> htmlRep
  }

  @Inject
  def injectxmlRep(@Named("xml") xmlRep: Representation) {
    representationModes += "xml" -> xmlRep
  }

  @Inject
  def injectRssRep(@Named("rss") rssRep: Representation) {
    representationModes += "rss" -> rssRep
  }

  def mode: Map[String, Representation] = {
    representationModes.toMap
  }

  def contentType: Map[String, String] = {
    contentTypes
  }
}