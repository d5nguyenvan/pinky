package org.pinky.code.extension.form


import annotation.form._
import hibernate.validator.{Length, ClassValidator}
import java.lang.reflect.Method
import java.lang.annotation.Annotation

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: May 28, 2009
 * Time: 1:29:53 PM
 * To change this template use File | Settings | File Templates.
 */


private[form] trait Builder {
  private[form] def manifest[T](implicit m: scala.reflect.Manifest[T]) = m.toString

  /**
   * defines standard control structure for building a form
   * @form the form class
   */
  private[form] def basedOn(form: Form, starttag: String, endtag: String): String = {
    var formBody = new StringBuffer()
    var action = ""
    var formType = "application/x-www-form-urlencoded"
    // add fields

    for (setter <- form.getClass.getMethods if setter.getName.contains("_$eq")) {
      for (getter <- form.getClass.getMethods if getter.getName == setter.getName.replace("_$eq", "")) {
        //check to see whether a custom action attribute needs to be set
        if (getter.getName.startsWith("action")) action = getter.invoke(form).toString
        for (annotation <- getter.getDeclaredAnnotations) {
          //check to see whether the form should be multipart
          if (annotation.annotationType == classOf[Upload]) formType = "multipart/form-data"
          //build the widget field
          widget(form, annotation, getter) match {
            case Some(field) => formBody.append(starttag + generateLabelFor(getter) + field + endtag)
            case None =>
          }
        }
      }
    }
    //assemble the final form
    <form action={action} method="POST" enctype={formType}>{scala.xml.Unparsed(formBody.toString)}</form>.toString
  }

  private[form] def widget(form: Form, annotation: Annotation, method: Method): Option[scala.xml.Elem] = annotation match {

    case a: Length =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="text" size={if (a.max <= 20) a.max.toString else "20"} maxlength={a.max.toString} name={method.getName.toLowerCase} value={method.invoke(form).toString}/>)

    case a: Hidden =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="hidden" name={method.getName.toLowerCase} value={method.invoke(form).toString}/>)

    case a: Upload =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="file" size="40" name={method.getName.toLowerCase} value={method.invoke(form).toString}/>)

    case a: DropDown => {
      //return nothing if the return type does not match
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("DropDown field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<option value={key.toLowerCase} selected=" ">{key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}</option>)
          else
            optionTags.append(<option value={key.toLowerCase}>{key.substring(0, 1).toUpperCase() + key.substring(1)}</option>)
        }
        if (a.multi)
          Some(<select name={method.getName.toLowerCase} multiple=" ">{scala.xml.Unparsed(optionTags.toString)}</select>)
        else
          Some(<select name={method.getName.toLowerCase}>{scala.xml.Unparsed(optionTags.toString)}</select>)
      } else
        throw new Exception("a DropDown should have a type of Map[String, Boolean]")
    }

    case a: RadioButton => {
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("Radiobutton field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<input type="radio" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase} selected=" "/>)
          else
            optionTags.append(<input type="radio" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}/>)
        }
        Some(scala.xml.XML.loadString(optionTags.toString))
      } else
        throw new Exception("a RadioButton should have a type of Map[String, Boolean]")
    }

    case a: CheckBox => {
      if (method.getReturnType == classOf[Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("CheckBox field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<input type="checkbox" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase} selected=" "/>)
          else
            optionTags.append(<input type="checkbox" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}/>)
        }
        Some(scala.xml.XML.loadString(optionTags.toString))
      } else
        throw new Exception("a CheckBox should have a type of Map[String, Boolean]")
    }

    case _ => None
  }

  private[form] def generateLabelFor(getter: Method): String = <label for={"id_" + getter.getName.toLowerCase}>{getter.getName.toLowerCase + ":"}</label>.toString

}



class Form(requestParams: scala.collection.jcl.Map[String, Array[String]]) {
  //awkward way to simulate an optional param but it works until 2.8 comes out
  def this() = {
    this (new scala.collection.jcl.HashMap())
  }

  for ((key, paramValues) <- requestParams) {
    for (setter <- this.getClass.getMethods if (setter.getName.toLowerCase.contains(key.toLowerCase + "_$eq"))) {
      if (isComplexWidget(setter)) {
        //first get the current field map if any
        for (getter <- this.getClass.getMethods if (getter.getName.toLowerCase == setter.getName.toLowerCase.replace("_$eq", ""))) {
          val currentField = getter.invoke(this).asInstanceOf[Map[String, Boolean]]
          //set values whenever is possible
          if (currentField != null) {
            for (param <- paramValues) {
              for (item <- currentField) {
                if (item._1 == param) currentField(item._1) = true
              }
            }
            //save field
            setter.invoke(this, currentField)
          } 
        }

      } else setter.invoke(this, paramValues(0))
    }
  }
  private def isComplexWidget(method: Method): Boolean = {
    for (annotation <- method.getDeclaredAnnotations
    if ((annotation.annotationType == classOf[CheckBox] ||
            annotation.annotationType == classOf[DropDown] ||
            annotation.annotationType == classOf[RadioButton]
            ) //manifest check for Map[String,Array[String] should come here
            )
    ) {
      return true
    }
    return false
  }

  def render: String = throw new Exception("this method should be overrriden")

}


/**
 * provides a form builder which outputs a form wrapped in a <tr><td>, note, you will need to provide the corresponding <table> tag
 */
trait TableBuilder extends Form with Builder {
  override def render: String = basedOn(this, "<tr><td>", "</td></tr>")
}

/**
 * provides a form builder which outputs a form wrapped in a paragraph tag
 */

trait ParagraphBuilder extends Form with Builder {
  override def render: String = basedOn(this, "<p>", "</p>")
}

/**
 * provides a form builder which outputs a form wrapped in a <li> tag, note, you will need to provide the corresponding <ul> tag
 */

trait UlTagBuilder extends Form with Builder {
  override def render: String = basedOn(this, "<li>", "</li>")

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
