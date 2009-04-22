package org.pinky.code.extension

import _root_.org.junit.Test
import _root_.scala.collection.jcl.HashMap
import java.io.{File, ObjectOutputStream, OutputStream, ByteArrayOutputStream}
import org.mockito.Mockito._
import org.scalatest.Spec
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:08:11 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnit4Runner])
class XmlRepresentationStraightScalaTest extends Spec with ShouldMatchers {
  describe("A scala based xml representation") {
    it("should_render_the_data") {
      val out = new ByteArrayOutputStream();
      val data = new HashMap[String, AnyRef]
      data += "message" -> "hello world"
      val representation = new XmlRepresentationStraightScala()
      representation.write(data, out)
      out.toString.contains("<map>") should be(true)
      out.toString.contains("<entry>") should be(true)
      out.toString.contains("<java.lang.String>message</java.lang.String>") should be(true)
      out.toString.contains("<java.lang.String>hello world</java.lang.String>") should be(true)
      out.toString.contains("</entry>") should be(true)
      out.toString.contains("</map>") should be(true)
    }
  }

}