package org.pinky.code.util
// In Scala we can specify multiple imports from the same package
// by combining them together.


/**
 * based on http://stackoverflow.com/questions/1163393/best-scala-imitation-of-groovys-safe-dereference-operator
 *
 */
object Elvis {
  def ?[T](block: => T): Option[T] =
    try {
      val memo = block
      if (memo == null) None
      else Some(memo)
    }
    catch { case e: NullPointerException => None }
}
