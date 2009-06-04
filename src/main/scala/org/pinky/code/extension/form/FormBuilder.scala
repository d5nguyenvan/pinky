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
                formBody.append(func(form,annotation,method))
            }  
        }
        //assemble the final form
        <form action={action} method="POST" enctype={formType}>{formBody}</form>.toString
    }
}


abstract class Form   {
  def render:String
}


trait TableBuilder extends Form with Render {
  /**
   * renders the form using <tr><td> tags. note thoug, you will need to wrap the whole form in your own <table></table>
   * tags
   *
   */
    override def render:String = {
      // the whole thing is execuated on the host class
      basedOn (this) {
       (form:Form,annotation:Annotation,m:Method) => annotation match  {
          case a:Length =>
                  <tr><td>
                  <label for={"id_"+fieldFor(m)}>{labelFor(m)}</label>
                  <input  id={"id_"+fieldFor(m)} type="text"  size={if (a.max <=20) a.max.toString else "20" } maxlength={a.max.toString} name={fieldFor(m)} value={m.invoke(form).toString}/>
                  </td></tr>.toString
          case a:Hidden =>
                <tr><td>
                <input  id={"id_"+fieldFor(m)} type="hidden"  name={fieldFor(m)} value={m.invoke(form).toString}/>
                </td></tr>.toString
          case a:Upload =>
               <tr><td>
                <label for={"id_"+fieldFor(m)}>{labelFor(m)}</label>
                <input  id={"id_"+fieldFor(m)} type="file"  size="40" name={fieldFor(m)} value={m.invoke(form).toString}/>
                </td></tr>.toString
          case a:DropDown =>
                  <tr><td>
                  </td></tr>.toString
          case a:RadioButton =>
                  <tr><td>
                  </td></tr>.toString
          case a:CheckBox =>
                  <tr><td>
                  </td></tr>.toString
          case _=>""

          }
      }
    }

}

trait ParagraphBuilder extends Form{

}                                                     

trait UlTagBuilder extends Form{
   
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
        
