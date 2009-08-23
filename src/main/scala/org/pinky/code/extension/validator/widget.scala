package org.pinky.code.extension.validator

import collection.mutable.Map
import org.pinky.code.util.Elvis.?
import org.hibernate.validator.{Validator, PropertyConstraint}
import org.pinky.code.annotation.form.{CheckBox, RadioButton, DropDown}

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Aug 23, 2009
 * Time: 2:13:59 PM
 * To change this template use File | Settings | File Templates.
 */

trait BaseValidator {
  def isValid(value:Object ):Boolean = {
     ?(value) match {
       case Some(map) =>
         if (map.isInstanceOf[Map[String,Boolean]]) {
            map.asInstanceOf[Map[String,Boolean]] find(kv => kv._2 ==true) match {case Some(kv)=>true;case None=>false}
         } else return false
       case None => false    
     }
  }

}
class DropDownValidator extends Validator[DropDown] with BaseValidator {
  def initialize(dropdown:DropDown)={}

}
class CheckBoxValidator extends Validator[CheckBox] with BaseValidator {
  def initialize(checkbox:CheckBox)={}

}
class RadioButtonValidator extends Validator[RadioButton] with BaseValidator {
  def initialize(radiobutton:RadioButton)={}

}