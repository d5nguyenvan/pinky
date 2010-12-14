package org.pinky.representation


/**
 * Main trait that provides various representations
 * See an example at    { @link DefaultRepresentations }
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
trait Representations {
  def mode: Map[String, Representation]
  def contentType: Map[String, String]
}
