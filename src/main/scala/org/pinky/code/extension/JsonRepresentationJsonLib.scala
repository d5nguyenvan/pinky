package org.pinky.code.extension

import java.io.{BufferedWriter, OutputStreamWriter, OutputStream}
import org.json.JSONObject
import scala.collection.jcl._

class JsonRepresentationJsonLib extends Representation {
  def write(data: Map[String, AnyRef], out: OutputStream) = {
    data.removeKey("template")
    val outWriter = new BufferedWriter(new OutputStreamWriter(out))
    outWriter.write((new JSONObject(data.asInstanceOf[MapWrapper[String, AnyRef]].underlying)).toString)
    outWriter.close

  }
}
