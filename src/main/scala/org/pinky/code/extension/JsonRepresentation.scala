package org.pinky.code.extension

import _root_.com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver

/**
 * Provides Json rendering, using the Jettison lib
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

class JsonRepresentation extends XmlRepresentation {
  xstream = new XStream(new JettisonMappedXmlDriver());
  xstream.setMode(XStream.NO_REFERENCES);
}