package org.pinky.code.extension
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:07:49 PM
 * To change this template use File | Settings | File Templates.
 */
import _root_.javax.servlet.ServletContext
import _root_.scala.collection.jcl.HashMap
import java.io.{File, OutputStream, ByteArrayOutputStream}
import org.pinky.code.extension._
import org.mockito.Mockito._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith

@RunWith(classOf[JUnit4Runner])
class HtmlRepresentationFreeMarkerTest extends Spec with ShouldMatchers {

  describe ("a Freemarker Representation") {
    var exceptionIsThrown = false
    it ("should throw an exception when template element is missing") {
      try {
        val ctx = mock(classOf[ServletContext])
        val rep = new HtmlRepresentationFreeMarker(ctx)
        val out = new ByteArrayOutputStream()
        val data = new HashMap[String,AnyRef]
        rep.write(data,out)
       } catch {
          case ex: NoSuchElementException => exceptionIsThrown = true
          case _ =>
      }  
      exceptionIsThrown should be (true)
    }

    it ("should fail with template key only") {
        var exceptionIsThrown = false
        try {
          val rep = new HtmlRepresentationFreeMarker(null)
          val out = new ByteArrayOutputStream()
          val data = new HashMap[String,AnyRef]
          data += "template"->"/hello/index.html"
          rep.write(data,out)
        } catch {
          case ex: freemarker.core.InvalidReferenceException => exceptionIsThrown = true
          case _ =>

        }
        exceptionIsThrown should be (true)
    }

    it ("with right params it should render") {
         val rep = new HtmlRepresentationFreeMarker(null)
         val out = new ByteArrayOutputStream()
         val data = new HashMap[String,AnyRef]
         data += "template"->"/hello/index.html"
         data += "message"->"hello world"
         rep.write(data,out)

    }
  }
}