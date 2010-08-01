package org.pinky.representation
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:08:26 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.ByteArrayOutputStream
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers  


class XmlRepresentationTest extends Spec with ShouldMatchers{

  describe ("An XML Representation"){
    it ("should_render_the_data") {
        val out = new ByteArrayOutputStream();
        val data = Map( "message" -> "hello world" )
        val representation = new XmlRepresentation()
        representation.write(data,out)
        out.toString should equal("<map>\n  <entry>\n    <string>message</string>\n    <string>hello world</string>\n  </entry>\n</map>")
    }
  }
}
