package org.pinky.code.extension.form


import hibernate.validator.{ClassValidator, InvalidValue}

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: May 28, 2009
 * Time: 1:29:53 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class Form   {
  
  def render():String = {
      render(this)
  }
  def render(form:Form):String = {

  }
  def validate():Map[String,String] = {
    validate(this)
  }
  //this will go away with 2.8's default parameters but I could not find a better way to execute this method on
  // the actual class 
  def validate(form:Form):Map[String,String]


}
 
trait TableBuilder extends Form {
    def render(form:Form):String = { ""
      //check methods
      //check annotation
      //check method type
      //generate snippet
    }
}

trait ParagraphBuilder extends Form{
    def render(form:Form):String = { ""
    }
}                                                     

trait UlTagBuilder extends Form{
    def render(form:Form):String = {
      ""
    }
}

trait Validator extends Form {

  def validate(form:Form):Map[String,String] = {
       var map:Map[String,String]=Map()
       val validationMessages = Factory.formValidator.getInvalidValues(form);
       for (message <- validationMessages) {
         map += message.getPropertyName -> message.getMessage
       }
       map
  }
  private object Factory {
    val formValidator = new ClassValidator( classOf[Form] );
  }
  
}
        
