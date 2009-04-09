package org.pinky.code.extension

import org.pinky.code.extension.Representation
import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}
import scala.collection.jcl._

class XmlRepresentationStraightScala extends Representation {
  def write(data: Map[String, AnyRef], out: OutputStream) = {
    data.removeKey("template")
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    var xml = new StringBuffer("<map>")
      data.foreach( (entry) => 
        {
          xml.append( "<entry>\n<java.lang.String>"+entry._1 + "</java.lang.String> \n" 
         +"<"+entry._2.getClass.getName+">"+ entry._2+"</"+entry._2.getClass.getName+">\n</entry>")
        }
       )
    xml.append("</map>")
    outWriter.write(xml.toString)
    outWriter.close

  }


}
