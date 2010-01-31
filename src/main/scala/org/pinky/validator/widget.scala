package org.pinky.validator

import collection.mutable.Map
import org.pinky.util.Elvis.?
import org.pinky.annotation.form.{CheckBox, RadioButton, DropDown}
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck
import net.sf.oval.Validator
import net.sf.oval.context.OValContext

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Aug 23, 2009
 * Time: 2:13:59 PM
 * To change this template use File | Settings | File Templates.
 */

trait BaseValidator {
  def isSatisfied(validatedObject: Object, value: Object, context: OValContext, validator: Validator): Boolean = {
    ?(value) match {
      case Some(map) =>
        if (map.isInstanceOf[Map[String, Boolean]]) {
          map.asInstanceOf[Map[String, Boolean]] find (kv => kv._2 == true) match {case Some(kv) => true; case None => false}
        } else return false
      case None => false
    }
  }

}
class DropDownValidator extends AbstractAnnotationCheck[DropDown] with BaseValidator
class CheckBoxValidator extends AbstractAnnotationCheck[CheckBox] with BaseValidator
class RadioButtonValidator extends AbstractAnnotationCheck[RadioButton] with BaseValidator