package org.pinky.representation

import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}

class XmlRepresentationStraightScala extends Representation {
  def write(rawData: Map[String, AnyRef], out: OutputStream) = {
    val data = rawData - ("template")
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    var xml = new StringBuffer("<map>")
    data.foreach {
      (entry) =>
        xml.append("<entry>\n<java.lang.String>" + entry._1 + "</java.lang.String> \n"
                + "<" + entry._2.getClass.getName + ">" + entry._2 + "</" + entry._2.getClass.getName + ">\n</entry>")
     }
    xml.append("</map>")
    outWriter.write(xml.toString)
    outWriter.close

  }


}
