package org.pinky.representation


import org.pinky.representation._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

class DefaultRepresentationsTest extends Spec with ShouldMatchers {
  describe("a default representation") {

    it ("should create modules") {
      var reps = new DefaultRepresentations()
      reps.injectJsonRep(new JsonRepresentation())
      reps.injecthtmlRep(new HtmlRepresentationFreeMarker(null))
      reps.injectxmlRep(new XmlRepresentation())
      reps.injectRssRep(new RssRepresentation())
      reps.mode("html").getClass should equal (classOf[HtmlRepresentationFreeMarker])
      reps.mode("xml").getClass should equal (classOf[XmlRepresentation])
      reps.mode("rss").getClass should equal (classOf[RssRepresentation])
      reps.mode("json").getClass  should equal (classOf[JsonRepresentation])
      reps.mode.size should equal(4)

    }

    it ("should_not_find_rss_module") {
      var reps = new DefaultRepresentations()
      reps.injectJsonRep(new JsonRepresentation())
      reps.injecthtmlRep(new HtmlRepresentationFreeMarker(null))
      reps.injectxmlRep(new XmlRepresentation())
      reps.injectRssRep(new XmlRepresentation())
      reps.mode("rss").getClass should not equal(classOf[JsonRepresentation])

    }
  }

}

