package org.pinky.code.util

/**
 *  based on <a href="http://www.saager.org/2007/12/30/automatic-resource-management-blocks.html">this article</a>
 * <br><br>
 *  generic ARM block to support calls like
 * <pre>
 * for (conn &lt;- using (ds.getConnection)   { //do something with datasource }
 * </pre>
 * or a nested one
 * <pre>
 * for (outer <- using (new PipeStream())   {
 *  for (inner <- using (new PipeStream())   {
 * //do something with outer and inner
 * }
 * }
 * </pre>
 */
trait ARM {
  /**
   * @resource the reasource that needs to be wrapped around
   */
  case class using[T <: {def close()}](resource: T) {
    /**
     * execute block in the proper scope
     */
    def foreach(f: T => Unit): Unit =
      try {
        f(resource)
      } finally {
        resource.close()
      }
  }
}
object ARM extends ARM
