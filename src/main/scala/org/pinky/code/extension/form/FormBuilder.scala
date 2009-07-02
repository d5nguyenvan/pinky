package org.pinky.code.extension.form


import annotation.form._
import hibernate.validator.{Length, ClassValidator}
import java.lang.reflect.Method
import java.lang.annotation.Annotation
import scala.collection.jcl.MapWrapper

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: May 28, 2009
 * Time: 1:29:53 PM
 * To change this template use File | Settings | File Templates.
 */


private[form] trait Render {
  private val GetterPattern = "get(.*)\\(".r

  /**change Method name to a world, useful for labels
   * @m method
   */
  private[form] def labelFor(m: Method): String = {fieldFor(m).substring(0, 1).toUpperCase}

  /**figure out field name from the method
   * @m method
   */
  private[form] def fieldFor(m: Method): String = {
    for (GetterPattern(field) <- GetterPattern findFirstIn m.getName) field.toLowerCase
    throw new Exception("could not find a getter for " + m.toString)
  }

  /**
   * defines standard control structure for building a form
   * @form the form class
   */
  private[form] def basedOn(form: Form)(renderWidget: Function3[Form, Annotation, Method, String]): String = {
    var formBody = new StringBuffer()
    var action = ""
    var formType = "application/x-www-form-urlencoded"
    // add fields
    for (method <- form.getClass.getMethods if method.getName.startsWith("get")) {
      //check to see whether a custom action attribute needs to be set
      if (method.getName.startsWith("getFormAction")) action = method.invoke(form).toString
      for (annotation <- method.getDeclaredAnnotations) {
        //check to see whether the form should be multipart
        if (annotation.annotationType == classOf[Upload]) formType = "multipart/form-data"
        //build the field widget
        formBody.append(renderWidget(form, annotation, method))
      }
    }
    //assemble the final form
    <form action={action} method="POST" enctype={formType}>{formBody}</form>.toString
  }

  private[form] def widget(form: Form, annotation: Annotation, method: Method): String = annotation match {

    case a: Length =>
      <input id={"id_" + fieldFor(method)} type="text" size={if (a.max <= 20) a.max.toString else "20"} maxlength={a.max.toString} name={fieldFor(method)} value={method.invoke(form).toString}/>.toString

    case a: Hidden =>
      <input id={"id_" + fieldFor(method)} type="hidden" name={fieldFor(method)} value={method.invoke(form).toString}/>.toString

    case a: Upload =>
      <input id={"id_" + fieldFor(method)} type="file" size="40" name={fieldFor(method)} value={method.invoke(form).toString}/>.toString

    case a: DropDown => {
      //return nothing if the return type does not match
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        for ((key, value) <- method.invoke(form).asInstanceOf[Map[String, Boolean]]) {
          if (value)
            optionTags.append(<option value={key.toLowerCase} selected=" ">{key.toUpperCase}</option>)
          else
            optionTags.append(<option value={key.toLowerCase}>{key.toUpperCase}</option>)
        }
        if (a.multi) <select multiple=" ">{optionTags}</select>.toString else <select>{optionTags}</select>.toString
      } else ""
    }

    case a: RadioButton => {
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        for ((key, value) <- method.invoke(form).asInstanceOf[Map[String, Boolean]]) {
          if (value)
            optionTags.append(<input type="radio" name={fieldFor(method)} value={key} selected=" "/>)
          else
            optionTags.append(<input type="radio" name={fieldFor(method)} value={key}/>)
        }
        optionTags.toString
      } else ""
    }

    case a: CheckBox => {
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        for ((key, value) <- method.invoke(form).asInstanceOf[Map[String, Boolean]]) {
          if (value)
            optionTags.append(<input type="checkbox" name={fieldFor(method)} value={key} selected=" "/>)
          else
            optionTags.append(<input type="checkbox" name={fieldFor(method)} value={key}/>)
        }
        optionTags.toString
      } else ""
    }

    case _ => ""
  }


}
private[form] trait Filler extends Form {
  private def isComplexWidget(method: Method): Boolean = {
    for (annotation <- method.getDeclaredAnnotations if (
            annotation.getClass == classOf[CheckBox] || annotation.getClass == classOf[DropDown] || annotation.getClass == classOf[RadioButton]
            )
    ) {
      true
    }
    false
  }

  override def fillObjectWith(requestParams: scala.collection.jcl.Map[String, Array[String]]) = {
    for ((key, value) <- requestParams) {
      for (setter <- this.getClass.getMethods if (setter.getName.startsWith("set") && setter.getName.toLowerCase.contains("set" + key.toLowerCase))) {
        if (isComplexWidget(setter)) {
          //first get the current Map if any
          for (getter <- this.getClass.getMethods if (getter.getName.startsWith("get") && getter.getName.toLowerCase.contains("get" + key.toLowerCase))) {
            //then set the chosen ones
            val map = getter.invoke(this)
            //set values whenever is possible
            for (item <-requestParams) {
              
            }
            //save the map
            setter.invoke(this, map)

          }


        } else setter.invoke(this, value(0))
      }
    }
  }
}


abstract class Form(requestParams: scala.collection.jcl.Map[String, Array[String]]) {
  //constructor, awkward way to simulate an optional param but it works
  def this() = {
    this (null)
  }

  fillObjectWith(requestParams)

  //methods
  def render: String

  def fillObjectWith(requestParams: scala.collection.jcl.Map[String,  Array[String]])
}


/**
 * renders the form using <tr><td> tags. note thoug, you will need to wrap the whole form in your own <table></table>
 * tags
 *
 */


trait TableBuilder extends Form with Filler with Render {
  override def render: String = basedOn(this) {
    (form: Form, annotation: Annotation, method: Method) =>
            <tr> <td> <label for={"id_" + fieldFor(method)}>{labelFor(method)}</label>{widget(form, annotation, method)}</td> </tr>.toString
  }
}

trait ParagraphBuilder extends Form with Render with Filler {
  override def render: String = basedOn(this) {
    (form: Form, annotation: Annotation, method: Method) =>
            <p> <label for={"id_" + fieldFor(method)}>{labelFor(method)}</label>{widget(form, annotation, method)}</p>.toString
  }
}

trait UlTagBuilder extends Form with Render with Filler {
  override def render: String = basedOn(this) {
    (form: Form, annotation: Annotation, method: Method) =>
            <li> <label for={"id_" + fieldFor(method)}>{labelFor(method)}</label>{widget(form, annotation, method)}</li>.toString
  }
}

trait Validator {
  def validate: Map[String, String] = {
    var map: Map[String, String] = Map()
    val validationMessages = Factory.formValidator.getInvalidValues(this);
    for (message <- validationMessages) {
      map += message.getPropertyName -> message.getMessage
    }
    map
  }
  private object Factory {
    val formValidator = new ClassValidator(classOf[AnyRef]);
  }

}
