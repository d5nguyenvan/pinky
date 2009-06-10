package org.pinky.code.extension.form


import annotation.form._  
import hibernate.validator.{Length, ClassValidator, InvalidValue}
import java.lang.reflect.Method
import java.lang.annotation.Annotation

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: May 28, 2009
 * Time: 1:29:53 PM
 * To change this template use File | Settings | File Templates.
 */


private[form] trait Render {
   private val GetterPattern = "get(.*)\\(".r

  /** change Method name to a world, useful for labels
   * @m method
   */
   private[form] def labelFor(m:Method):String = {fieldFor(m).substring(0,1).toUpperCase}

  /** figure out field name from the method
   * @m method
   */
   private[form] def fieldFor(m:Method):String = {
     for (GetterPattern(field) <-GetterPattern findFirstIn m.getName) field.toLowerCase
     throw new Exception("could not find a getter for "+m.toString)
   }

  /**
   * defines standard control structure for building a form 
   * @form the form class
   */
   private[form] def basedOn(form:Form) (func:Function3[Form,Annotation,Method, String]) :String = {
        var formBody = new StringBuffer()
        var action = ""    
        var formType ="application/x-www-form-urlencoded"
        // add fields
        for (method <- form.getClass.getMethods if method.getName.startsWith("get")) {
            //check to see whether a custom action attribute needs to be set
            if (method.getName.startsWith("getFormAction")) action = method.invoke(form).toString
            for (annotation <- method.getAnnotations ) {
                //check to see whether the form should be multipart
                if (annotation.annotationType == classOf[Upload]) formType = "multipart/form-data"
                //build the field widget              
                formBody.append(<label for={"id_"+fieldFor(method)}>{labelFor(method)}</label>.toString+func(form,annotation,method))
            }  
        }
        //assemble the final form
        <form action={action} method="POST" enctype={formType}>{formBody}</form>.toString
    }

  private[form] def widget(form:Form,annotation:Annotation,method:Method):String = annotation match {
    case a:Length =>
      <input  id={"id_"+fieldFor(method)} type="text"  size={if (a.max <=20) a.max.toString else "20" } maxlength={a.max.toString} name={fieldFor(method)} value={method.invoke(form).toString}/>.toString
    case a:Hidden =>
      <input  id={"id_"+fieldFor(method)} type="hidden"  name={fieldFor(method)} value={method.invoke(form).toString}/>.toString
    case a:Upload =>
      <input  id={"id_"+fieldFor(method)} type="file"  size="40" name={fieldFor(method)} value={method.invoke(form).toString}/>.toString
    case a:DropDown =>
      <select><option value="audi">Audi</option></select>.toString
    case a:RadioButton =>
      <input type="radio" name="group1" value="Milk" /><input type="radio" name="group1" value="Butter" checked="" />.toString
    case a:CheckBox =>
       <input type="checkbox" name="group1" value="Milk" /><input type="radio" name="group1" value="Butter" checked="" />.toString
    case _=>""
  }

}


abstract class Form   {
  def render:String
}

 /**
   * renders the form using <tr><td> tags. note thoug, you will need to wrap the whole form in your own <table></table>
   * tags
   *
   */

trait TableBuilder extends Form with Render {
    override def render:String = basedOn (this) {
      (form:Form,annotation:Annotation,method:Method) => <tr><td>{widget(form,annotation,method)}</td></tr>.toString
    }
}

trait ParagraphBuilder extends Form with Render {
    override def render:String = basedOn (this) {
      (form:Form,annotation:Annotation,method:Method) => <p>{widget(form,annotation,method)}</p>.toString
    }
}

trait UlTagBuilder extends Form with Render {
     override def render:String = basedOn (this) {
       (form:Form,annotation:Annotation,method:Method) => <li>{widget(form,annotation,method)}</li>.toString
     }
}

trait Validator {

  def validate:Map[String,String] = {
       var map:Map[String,String]=Map()
       val validationMessages = Factory.formValidator.getInvalidValues(this);
       for (message <- validationMessages) {
         map += message.getPropertyName -> message.getMessage
       }
       map
  }
  private object Factory {
    val formValidator = new ClassValidator( classOf[AnyRef] );
  }

}