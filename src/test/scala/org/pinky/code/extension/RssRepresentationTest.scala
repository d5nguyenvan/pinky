package org.pinky.code.extension


import _root_.org.junit.Test
import _root_.scala.collection.jcl.HashMap
import java.io.{File, ObjectOutputStream, OutputStream, ByteArrayOutputStream}
import java.text.DateFormat
import java.util.Date
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 11:49:56 AM
 * To change this template use File | Settings | File Templates.
 */

class RssRepresentationTest  {
  @Test {val expected=classOf[java.util.NoSuchElementException]}
  def  should_fail_due_to_missing_items() {
      val out = new ByteArrayOutputStream();
      val data = new HashMap[String,AnyRef]
      data += "message" -> "hello world"
      val representation = new RssRepresentation()
      representation.write(data,out)
      throw new Exception ("it should fail now by now")
  }
  @Test {val expected=classOf[java.lang.ClassCastException]}
  def  should_fail_since_item_type_is_not_list_rss_items() {
      val out = new ByteArrayOutputStream();
      val data = new HashMap[String,AnyRef]
      data += "rssitems" -> "hello world"
      val representation = new RssRepresentation()
      representation.write(data,out)
      throw new Exception ("it should fail now by now")
  }
  @Test
  def  should_render_rss() {
     //today's date
      val now = new Date();
      val today = DateFormat.getDateTimeInstance(
          DateFormat.LONG, DateFormat.LONG).format(now)

      //create  header
      var rssHeader = new RssHeader("Test","http://lolstation.com", "MISC MISC", today, today, "(C) 2009", "en-us")

      //create items
      val rssList = List[RssItem](
        new RssItem("item title","http://localstation.com/item11","description",
        today,"http://localstation.com/item11#1")
      )

      //setup return values
      val data = new HashMap[String, AnyRef]
      data += "rssitems" -> rssList
      data += "rssheader" -> rssHeader
      val out = new ByteArrayOutputStream();

      val representation = new RssRepresentation()
      representation.write(data,out)
      assertThat(out.toString, containsString("<title>Test </title>"))
      assertThat(out.toString, containsString("<description>MISC MISC</description>"))
      assertThat(out.toString, containsString("<guid>http://localstation.com/item11#1</guid>"))
  }
}


