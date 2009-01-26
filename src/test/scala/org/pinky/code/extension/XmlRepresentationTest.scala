package org.pinky.code.extension
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:08:26 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.org.junit.Test
import _root_.scala.collection.jcl.HashMap
import java.io.{File, ObjectOutputStream, OutputStream, ByteArrayOutputStream}
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

class XmlRepresentationTest {


   @Test
  def it_should_render_the_data() {
      val out = new ByteArrayOutputStream();
      val data = new HashMap[String,AnyRef]
      data += "message" -> "hello world"
      val representation = new XmlRepresentation()
      representation.write(data,out)
      assertThat(out.toString, is("<map>\n  <entry>\n    <string>message</string>\n    <string>hello world</string>\n  </entry>\n</map>"))
  }
}