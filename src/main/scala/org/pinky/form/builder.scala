package org.pinky.form.builder


import java.lang.reflect.Method
import java.lang.annotation.Annotation
import scala.collection.mutable
import org.pinky.annotation.form._
import net.sf.oval.constraint.Length
import net.sf.oval.ConstraintViolation
import org.pinky.validator.{CustomOvalValidator}
import collection.jcl.Conversions._

/**
 * defines default behaviour for prepopulating and rendering forms
 */
private[form] trait Default {

  /**
   * @requestParams incoming request param's param map
   *  setting form data using reflection 
   */
  def loadRequest(requestParams: java.util.Map[String, Array[String]]) = {
    for ((key, paramValues) <- requestParams) {
      for (setter <- this.getClass.getMethods if (setter.getName.toLowerCase.contains(key.toLowerCase + "_$eq"))) {
        if (isComplexWidget(setter)) {
          //first get the current field map if any
          for (getter <- this.getClass.getMethods if (getter.getName.toLowerCase == setter.getName.toLowerCase.replace("_$eq", ""))) {
            var currentField = getter.invoke(this).asInstanceOf[mutable.Map[String, Boolean]]
            //set values whenever is possible
            if (currentField != null) {
              for (param <- paramValues) {
                for (item <- currentField) {if (item._1 == param) currentField(item._1) = true}
              }
              //save field
              setter.invoke(this, currentField)
            }
          }
        } else setter.invoke(this, paramValues(0))
      }
    }
  }


  /**
   *  by complex widget we mean widgets that can vary in terms of size
   */
  private def isComplexWidget(method: Method): Boolean = {
    for (annotation <- method.getDeclaredAnnotations
         if ((annotation.annotationType == classOf[CheckBox] ||
                 annotation.annotationType == classOf[DropDown] ||
                 annotation.annotationType == classOf[RadioButton]
                 )
                 )
    ) {
      return true
    }
    return false
  }


  /**
   *  defines standard control structure for building a form
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
            case Some(field) => formBody.append(starttag + "\n" + generateLabelFor(getter) + "\n" + scala.xml.Unparsed(field) + "\n" + endtag + "\n")
            case None =>
          }
        }
      }
    }
    //assemble the final form
    <form action={action} method="POST" enctype={formType}>
      {scala.xml.Unparsed(formBody.toString)}
    </form>.toString
  }

  /**
   * @form incoming form
   * @annotation the annotation on the widget
   * @method which is referencing the current widget
   * renders the widget
   */
  private[form] def widget(form: Form, annotation: Annotation, method: Method): Option[String] = annotation match {

    case a: Length =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="text" size={if (a.max <= 20) a.max.toString else "20"} maxlength={a.max.toString} name={method.getName.toLowerCase} value={method.invoke(form).toString}/>.toString)

    case a: Hidden =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="hidden" name={method.getName.toLowerCase} value={method.invoke(form).toString}/>.toString)

    case a: Upload =>
      Some(<input id={"id_" + method.getName.toLowerCase} type="file" size="40" name={method.getName.toLowerCase} value={method.invoke(form).toString}/>.toString)

    case a: TextArea =>
      Some(<textarea id={"id_" + method.getName.toLowerCase} name={method.getName.toLowerCase} rows={a.rows.toString} cols={a.cols.toString}/>.toString)

    case a: DropDown => {
      //return nothing if the return type does not match
      if (method.getReturnType == classOf[mutable.Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[mutable.Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("DropDown field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<option value={key.toLowerCase} selected=" ">
              {key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}
            </option> + "\n")
          else
            optionTags.append(<option value={key.toLowerCase}>
              {key.substring(0, 1).toUpperCase() + key.substring(1)}
            </option> + "\n")
        }
        if (a.multi)
          Some(<select name={method.getName.toLowerCase} multiple=" ">
            {scala.xml.Unparsed(optionTags.toString)}
          </select>.toString)
        else
          Some(<select name={method.getName.toLowerCase}>
            {scala.xml.Unparsed(optionTags.toString)}
          </select>.toString)
      } else
        throw new Exception("a DropDown should have a type of Map[String, Boolean]")
    }

    case a: RadioButton => {
      if (method.getReturnType == classOf[mutable.Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[mutable.Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("Radiobutton field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<input type="radio" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase} selected=" "/> + "\n")
          else
            optionTags.append(<input type="radio" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}/> + "\n")
        }
        Some(optionTags.toString)
      } else
        throw new Exception("a RadioButton should have a type of Map[String, Boolean]")
    }

    case a: CheckBox => {
      if (method.getReturnType == classOf[mutable.Map[String, Boolean]]) {
        val optionTags = new StringBuffer()
        val map = method.invoke(form).asInstanceOf[mutable.Map[String, Boolean]]
        if (map == null || map.size == 0) throw new Exception("CheckBox field needs at least one item")
        for ((key, value) <- map) {
          if (value)
            optionTags.append(<input type="checkbox" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase} selected=" "/> + "\n")
          else
            optionTags.append(<input type="checkbox" name={method.getName.toLowerCase} value={key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase}/> + "\n")
        }
        Some(optionTags.toString)
      } else
        throw new Exception("a CheckBox should have a type of Map[String, Boolean]")
    }

    case _ => None
  }

  private[form] def generateLabelFor(getter: Method): String = <label for={"id_" + getter.getName.toLowerCase}>
    {getter.getName.toLowerCase + ":"}
  </label>.toString

}

/**
 * establishes a main base class for all form actions
 */
abstract class Form

/**
 * describes the main behavior of a builder
 */
trait Builder {
  /**
   * renders the form
   */
  def render: String

  /**
   * @requestParam incoming request params
   * prepopulates bean data based on a request's requestParamMap
   */
  def loadRequest(requestParams: java.util.Map[String, Array[String]])
}


/**
 * provides a form builder which outputs a form wrapped in a <pre><tr><td></pre>, note, you will need to provide the corresponding <table> tag
 */
trait TableBuilder extends Form with Builder with Default {
  override def render: String = basedOn(this, "<tr><td>", "</td></tr>")
}

/**
 * provides a form builder which outputs a form wrapped in a paragraph tag
 */

trait ParagraphBuilder extends Form with Builder with Default {
  override def render: String = basedOn(this, "<p>", "</p>")
}

/**
 * provides a form builder which outputs a form wrapped in a <pre><li></li></pre> tag, note, you will need to provide the corresponding <ul> tag
 */

trait UlTagBuilder extends Form with Builder with Default {
  override def render: String = basedOn(this, "<li>", "</li>")

}


/**
 * provides validation using oval framework
 */
trait Validator {
  import java.util.{List => JList, Map => JMap, HashMap, ArrayList}
  import collection.jcl.Conversions._

  /**
   *
   * @return it returns a java list[Map] because it's most likely used from a java templating enginge
   */
  def validate: JList[JMap[String, String]] = {
    val list = new ArrayList[JMap[String, String]]()
    val validationMessages = Factory.validator.validateFor(this)
    for (message <- validationMessages) list.add(extract(message))
    list
  }

  private def extract(m: ConstraintViolation): JMap[String, String] = {
    val map = new HashMap[String, String]()
    map.put(m.getMessage.substring(m.getMessage.lastIndexOf(".") + 1, m.getMessage.indexOf(" "))
      , m.getMessage.substring(m.getMessage.lastIndexOf(".") + 1))
    map
  }

  private object Factory {
    val validator = new CustomOvalValidator()
  }
}
