package org.pinky.code.extension.controlstructure

import _root_.javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.jcl._

 /**
 * Trait which defines the control structure layout
 *
 * @author peter hausel gmail com (Peter Hausel)
 */


trait BaseControl {

    def call(request: HttpServletRequest, response:HttpServletResponse) (block: =>  Map[String,AnyRef])


}