package org.pinky.representation

import java.io.ByteArrayOutputStream
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers  

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:08:11 PM
 * To change this template use File | Settings | File Templates.
 */

class JsonRepresentationTest extends Spec with ShouldMatchers {
  describe("A JSON representation") {
    it("should_render_the_data") {
      val out = new ByteArrayOutputStream();
      val data = Map("message" -> "hello world" )
      val representation = new JsonRepresentation()
      representation.write(data, out)
      out.toString should equal("{\"map\": [\n  [\n    \"message\",\n    \"hello world\"\n  ]\n]}")
    }
  }
}
