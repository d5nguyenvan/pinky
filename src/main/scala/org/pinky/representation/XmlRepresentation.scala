package org.pinky.representation

import _root_.com.thoughtworks.xstream.XStream
import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}
import collection.JavaConversions._
import java.util.HashMap

/**
 * Provides an XML representation using xstream,
 * you can utilize this module by registering .xml extension in your Servlet mapping in your listener
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

class XmlRepresentation extends Representation {
  protected var xstream = new XStream();

  /**
   * @param data data coming from the user
   * @param out outputstream used to print out the response
   */
  def write(rawdata: Map[String, AnyRef], out: OutputStream) = {
    val data = rawdata - ("template")
    val javaMap = new HashMap[String,Object]
    data foreach {case (key, value) => javaMap.put(key,value)}
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    outWriter.write(xstream.toXML(javaMap))
    outWriter.close

  }
}