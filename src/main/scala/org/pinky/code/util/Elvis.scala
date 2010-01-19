package org.pinky.code.util

/**
 * based on <a href="http://stackoverflow.com/questions/1163393/best-scala-imitation-of-groovys-safe-dereference-operator">this article</a>
 * <br><br>
 * wrap chained and null method calls into an Option type
 * after importing this
 * <pre>
 * val whatsthis = ?(method.a.b.c) match  { case Some(s) =>s;case None=>"boooo" }
 * </pre>
 */
object Elvis {
  /**
   * @block code block that's being executed and wrapped around
   */
  def ?[T](block: => T): Option[T] =
    try {
      val memo = block
      if (memo == null) None
      else Some(memo)
    }
    catch {case e: NullPointerException => None}
}
