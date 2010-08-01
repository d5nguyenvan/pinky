package org.pinky.representation

import java.io.OutputStream

/**
 * Main trait which defines a representation
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

trait Representation {
  def write(data: Map[String, AnyRef], out: OutputStream)
}
