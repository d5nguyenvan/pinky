package org.pinky.representation

import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}
import org.json.JSONObject

class JsonRepresentationJsonLib extends Representation {
  def write(rawdata: Map[String, AnyRef], out: OutputStream) = {
    val data = rawdata - ("template")
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    outWriter.write((new JSONObject(data)).toString)
    outWriter.close

  }
}
