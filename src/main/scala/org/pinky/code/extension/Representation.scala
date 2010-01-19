package org.pinky.code.extension

import java.io.OutputStream
import scala.collection.jcl._

/**
 * Main trait which defines a representation
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

trait Representation {
  def write(data: Map[String, AnyRef], out: OutputStream)
}
