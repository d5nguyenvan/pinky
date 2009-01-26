package org.pinky.code.extension

import _root_.javax.servlet.ServletContext
import _root_.org.junit.Test
import _root_.scala.collection.jcl.HashMap
import java.io.{File, ObjectOutputStream, OutputStream}
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:04:33 PM
 * To change this template use File | Settings | File Templates.
 */

class HtmlRepresentationVelocityTest {
    @Test {val expected=classOf[java.util.NoSuchElementException]}
    def it_should_throw_exception_due_to_missing_template() {
      val ctx = mock(classOf[ServletContext])
      when(ctx.getRealPath("/")).thenReturn(new File (".").getCanonicalPath+"/")
      val representation = new HtmlRepresentationVelocity(ctx)
      val out = mock(classOf[OutputStream])
      val data = new HashMap[String,AnyRef]()
      representation.write(data,out)
      throw new Exception ("it should have failed earlier")

    }
    @Test {val expected=classOf[org.apache.velocity.exception.ResourceNotFoundException]}
    def it_should_fail_without_proper_template() {
      val ctx = mock(classOf[ServletContext])
      when(ctx.getRealPath("/")).thenReturn(new File (".").getCanonicalPath+"/")
      val representation = new HtmlRepresentationVelocity(ctx)
      val out = mock(classOf[OutputStream])
      val data = new HashMap[String,AnyRef]
      data += "template"->"/hello/index11.html"
      representation.write(data,out)
      throw new Exception ("it should have failed earlier")
    }
    @Test
    def it_should_render_the_template() {
      val ctx = mock(classOf[ServletContext])
      val path =new File (".").getCanonicalPath+"/src/main/webapp/"
      when(ctx.getRealPath("/")).thenReturn(path)
      val representation = new HtmlRepresentationVelocity(ctx)
      val out = mock(classOf[OutputStream])
      val data = new HashMap[String,AnyRef]
      data += "template"->"/hello/index.html"
      data += "message"->"hello world"
      representation.write(data,out)
      verify(out).flush()
    }
}