package org.pinky.representation

import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}
import org.json.JSONObject
import collection.JavaConversions._

class JsonRepresentationJsonLib extends Representation {
  def write(rawdata: Map[String, AnyRef], out: OutputStream) = {
    val data = rawdata - ("template")
    val javaMap:java.util.Map[String,Object] = data
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    outWriter.write((new JSONObject(javaMap).toString))
    outWriter.close

  }
}
