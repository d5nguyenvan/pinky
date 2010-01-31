package org.pinky.representation

import _root_.javax.servlet.ServletContext
import _root_.scala.collection.jcl.HashMap
import java.io.{File, OutputStream}
import org.mockito.Mockito._
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers


/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:04:33 PM
 * To change this template use File | Settings | File Templates.
 */

class HtmlRepresentationVelocityTest extends Spec with ShouldMatchers {
  describe("A velocity representation") {
    it("should_throw_exception_due_to_missing_template") {
      var exceptionIsThrown = false
      try {
        val ctx = mock(classOf[ServletContext])
        when(ctx.getRealPath("/")).thenReturn(new File(".").getCanonicalPath + "/")
        val representation = new HtmlRepresentationVelocity(ctx)
        val out = mock(classOf[OutputStream])
        val data = new HashMap[String, AnyRef]()
        representation.write(data, out)
      } catch {
        case ex: NoSuchElementException => exceptionIsThrown = true
        case _ =>
      }
      exceptionIsThrown should be(true)
    }
    it("should_fail_without_proper_template") {
      var exceptionIsThrown = false
      try {
        val ctx = mock(classOf[ServletContext])
        when(ctx.getRealPath("/")).thenReturn(new File(".").getCanonicalPath + "/")
        val representation = new HtmlRepresentationVelocity(ctx)
        val out = mock(classOf[OutputStream])
        val data = new HashMap[String, AnyRef]
        data += "template" -> "/hello/index11.html"
        representation.write(data, out)
      } catch {
        case ex: org.apache.velocity.exception.ResourceNotFoundException => exceptionIsThrown = true
        case _ =>
      }
      exceptionIsThrown should be(true)

    }
    it("should_render_the_template") {
      val ctx = mock(classOf[ServletContext])
      val path = new File(".").getCanonicalPath + "/src/main/webapp/"
      when(ctx.getRealPath("/")).thenReturn(path)
      val representation = new HtmlRepresentationVelocity(ctx)
      val out = mock(classOf[OutputStream])
      val data = new HashMap[String, AnyRef]
      data += "template" -> "/hello/index.html"
      data += "message" -> "hello world"
      representation.write(data, out)
      verify(out).flush()
    }
  }
}
