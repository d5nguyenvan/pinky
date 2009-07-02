package org.pinky.code.extension
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:08:26 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.scala.collection.jcl.HashMap
import java.io.ByteArrayOutputStream
import org.scalatest.Spec
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers  

@RunWith(classOf[JUnit4Runner])
class XmlRepresentationTest extends Spec with ShouldMatchers{

  describe ("An XML Representation"){
    it ("should_render_the_data") {
        val out = new ByteArrayOutputStream();
        val data = new HashMap[String,AnyRef]
        data += "message" -> "hello world"
        val representation = new XmlRepresentation()
        representation.write(data,out)
        out.toString should equal("<map>\n  <entry>\n    <string>message</string>\n    <string>hello world</string>\n  </entry>\n</map>")
    }
  }
}