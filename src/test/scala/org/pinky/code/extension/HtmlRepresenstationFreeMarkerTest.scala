package org.pinky.code.extension
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:07:49 PM
 * To change this template use File | Settings | File Templates.
 */
import _root_.javax.servlet.ServletContext
import _root_.org.junit.{Test}
import _root_.scala.collection.jcl.HashMap
import java.io.{File, OutputStream, ByteArrayOutputStream}
import org.pinky.code.extension._
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
class HtmlRepresentationFreeMarkerTest {
    @Test {val expected=classOf[NoSuchElementException]}
    def it_should_throw_an_exception {
         val ctx = mock(classOf[ServletContext])
         val rep = new HtmlRepresentationFreeMarker(ctx)
         val out = new ByteArrayOutputStream()
         val data = new HashMap[String,AnyRef]
         rep.write(data,out)
    }
    @Test {val expected=classOf[freemarker.core.InvalidReferenceException]}
    def it_should_fail_with_message_key_missing {
         val rep = new HtmlRepresentationFreeMarker(null)
         val out = new ByteArrayOutputStream()
         val data = new HashMap[String,AnyRef]
         data += "template"->"/hello/index.html"
         rep.write(data,out)
    }
     @Test 
    def it_should_render {
         val rep = new HtmlRepresentationFreeMarker(null)
         val out = new ByteArrayOutputStream()
         val data = new HashMap[String,AnyRef]
         data += "template"->"/hello/index.html"
         data += "message"->"hello world"
         rep.write(data,out)

    }

}