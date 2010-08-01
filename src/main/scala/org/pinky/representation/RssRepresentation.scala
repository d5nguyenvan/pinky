package org.pinky.representation

import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}


/**
 * Provides an RSS 2.0 representation using Scala's XML support,
 * you can utilize this module by registering .rss extension in your Servlet mapping in your listener
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
class RssRepresentation extends Representation {
  def write(rawdata: Map[String, AnyRef], out: OutputStream) = {
    val data = rawdata - ("template")
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    outWriter.write(rssTemplate(data))
    outWriter.close
  }


  /**
   * @param data data coming from the user
   * this method assembles the feed. Right now the xml is kind of awkwardly formatted due to
   * the multi-line sting. Would make sense to clean it up
   * @return is the whole feed
   */
  protected def rssTemplate(data: Map[String, AnyRef]): String = {
    val items = data("rssitems").asInstanceOf[List[RssItem]]
    val header = data("rssheader").asInstanceOf[RssHeader]

    val headerTags =
    """<?xml version="1.0"?>
       <rss version="2.0">
       <channel>
       """

    val footerTags =
    """</channel>
       </rss>"""

    val headerRss =
    <title>
      {header.title}
    </title>
            <link>
              {header.link}
            </link>
            <description>
              {header.description}
            </description>
            <language>
              {header.language}
            </language>
            <copyright>
              {header.copyright}
            </copyright>
            <pubDate>
              {header.pubdate}
            </pubDate>
            <lastBuildDate>
              {header.lastdate}
            </lastBuildDate>
            <docs>http://blogs.law.harvard.edu/tech/rss</docs>;

    return headerTags + headerRss.mkString + "\n" + printItems(items) + "\n" + footerTags
  }


  /**
   * @param items feed items which will show up as entries in the feed
   * @return will be the body of the feed
   */
  private def printItems(items: List[RssItem]): String = {

    var ret = new StringBuffer()
    for (item <- items) {
      val xmlItem =
      <item>
        <title>
          {item.itemtitle}
        </title>
        <link>
          {item.itemlink}
        </link>
        <description>
          {item.itemdescription}
        </description>
        <pubDate>
          {item.itempubdate}
        </pubDate>
        <guid>
          {item.itemguid}
        </guid>
      </item>;
      ret.append(xmlItem.toString)
    }
    ret.toString

  }

}