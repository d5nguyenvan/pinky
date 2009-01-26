package org.pinky.code.extension





import _root_.javax.servlet.ServletContext
import _root_.org.junit.Test
import org.pinky.code.extension._
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

class DefaultRepresentationsTest {

@Test
def it_should_create_modules {
   var reps = new DefaultRepresentations()
   reps.injectJsonRep(new JsonRepresentation())
   reps.injecthtmlRep(new HtmlRepresentationFreeMarker(null))
   reps.injectxmlRep(new XmlRepresentation())
   reps.injectRssRep(new RssRepresentation())
   assertThat (reps.mode("html"), is(classOf[HtmlRepresentationFreeMarker]) )
   assertThat (reps.mode("xml"), is(classOf[XmlRepresentation]) )
   assertThat (reps.mode("rss"), is(classOf[RssRepresentation]) )
   assertThat (reps.mode("json"), is(classOf[JsonRepresentation]) )
   assertThat (reps.mode.size, is(4))

}

  @Test
  def it_should_not_find_rss_module {
     var reps = new DefaultRepresentations()
     reps.injectJsonRep(new JsonRepresentation())
     reps.injecthtmlRep(new HtmlRepresentationFreeMarker(null))
     reps.injectxmlRep(new XmlRepresentation())
     reps.injectRssRep(new XmlRepresentation())
     assertThat (reps.mode("rss"), not( is(classOf[JsonRepresentation]) ) )

  }

}

