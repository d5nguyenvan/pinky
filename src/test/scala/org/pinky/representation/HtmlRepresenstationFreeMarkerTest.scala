package org.pinky.representation
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:07:49 PM
 * To change this template use File | Settings | File Templates.
 */
import _root_.javax.servlet.ServletContext
import java.io.ByteArrayOutputStream
import org.pinky.representation._
import org.mockito.Mockito._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

class HtmlRepresentationFreeMarkerTest extends Spec with ShouldMatchers {

  describe ("a Freemarker Representation") {
    var exceptionIsThrown = false
    it ("should throw an exception when template element is missing") {
      try {
        val ctx = mock(classOf[ServletContext])
        val rep = new HtmlRepresentationFreeMarker(ctx)
        val out = new ByteArrayOutputStream()
        val data = Map[String,AnyRef]()
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
          val data = Map("template"->"/hello/index.html")
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
         val data = Map("template"->"/hello/index.html","message"->"hello world")
         rep.write(data,out)

    }
  }
}
