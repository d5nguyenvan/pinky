package org.pinky.representation

import _root_.com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver

/**
 * Provides Json rendering, using the Jettison lib
 *
 * @author peter hausel gmail com (Peter Hausel)
 */

class JsonRepresentation extends XmlRepresentation {
  xstream = new XStream(new JsonHierarchicalStreamDriver());
  xstream.setMode(XStream.NO_REFERENCES);
}
