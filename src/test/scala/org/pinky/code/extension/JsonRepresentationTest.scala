package org.pinky.code.extension

import _root_.scala.collection.jcl.HashMap
import java.io.ByteArrayOutputStream
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
class JsonRepresentationTest extends Spec with ShouldMatchers {
  describe("A JSON representation") {
    it("should_render_the_data") {
      val out = new ByteArrayOutputStream();
      val data = new HashMap[String, AnyRef]
      data += "message" -> "hello world"
      val representation = new JsonRepresentation()
      representation.write(data, out)
      out.toString should equal("{\"map\": [\n  [\n    \"message\",\n    \"hello world\"\n  ]\n]}")
    }
  }
}
