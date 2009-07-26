package org.pinky.code.util

/**
 *  based on http://www.saager.org/2007/12/30/automatic-resource-management-blocks.html
 */
object ARM {
  case class using[T <: {def close()}](resource: T) {
    def foreach(f: T => Unit): Unit =
      try {
        f(resource)
      } finally {
        resource.close()
      }
  }
}
