package org.pinky.code.extension.form

import annotation.form._
import com.jteigen.scalatest.JUnit4Runner
import hibernate.validator.Length
import javax.servlet.http.HttpServletRequest
import junit.runner.RunWith
import scalatest.matchers.ShouldMatchers
import scalatest.Spec

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jul 2, 2009
 * Time: 11:54:23 AM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(classOf[JUnit4Runner])
class FormTest extends Spec with ShouldMatchers {
  class NewForm extends Form {
    @RadioButton
    var radioButton: Map[String, Boolean] = _

    @Length {val max = 20}
    var firstName: String = _

    @Length {val max = 20}
    var lastName: String = _
  }

  class ValidFormRadioButton extends Form {
    @RadioButton
    var radioButton: Map[String, Boolean] = Map("name" -> false)

    @Length {val max = 20}
    var firstName: String = _

    @Length {val max = 20}
    var lastName: String = _
  }

  class ValidDropDown extends Form {

    
    @DropDown {val multi = false}
    var drop: Map[String, Boolean] = Map("ko" -> false)

    @Length {val max = 20}
    var firstName: String = _

    @Length {val max = 20}
    var lastName: String = _
  }
  class ValidCheckBox extends Form {
    @CheckBox {val multi = false}
    var people: Map[String, Boolean] = Map("name" -> false)

    @Length {val max = 20}
    var firstName: String = _

    @Length {val max = 20}
    var lastName: String = _
  }


  describe("a Form") {
    it ("should work with map"){
       val requestParams:scala.collection.jcl.Map[String,Array[String]] = new scala.collection.jcl.HashMap()
       requestParams.put("drop",Array("name"))
       val form = new ValidDropDown(requestParams) with TableBuilder
       form.drop should equal (Map("name"->true) )
    }
    it("should complain about empty radioButton") {
      val form = new NewForm
      form.firstName = "lol"
      form lastName = "yeah"
      try {
        form.render
      } catch {
        case ex: Exception =>
        case _ => throw new Exception()
      }
    }
    it("should show a form with a radiobutton") {
      val form = new ValidFormRadioButton() with TableBuilder
      form.firstName = "lol"
      form.lastName = "yeah"
      form.render should equal("<form action=\"\" enctype=\"application/x-www-form-urlencoded\" method=\"POST\"><tr><td><label for=\"id_firstname\">firstname:</label><input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input></td></tr><tr><td><label for=\"id_lastname\">lastname:</label><input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input></td></tr><tr><td><label for=\"id_radiobutton\">radiobutton:</label><input name=\"radiobutton\" type=\"radio\" value=\"Name\"></input></td></tr></form>")
    }
    it("should show a form with a DropDown") {
      val form = new ValidDropDown() with TableBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      form.render should equal("<form action=\"\" enctype=\"application/x-www-form-urlencoded\" method=\"POST\"><tr><td><label for=\"id_firstname\">firstname:</label><input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input></td></tr><tr><td><label for=\"id_lastname\">lastname:</label><input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input></td></tr><tr><td><label for=\"id_radiobutton\">radiobutton:</label><select name=\"radiobutton\"><option value=\"name\">Name</option></select></td></tr></form>")
    }
    it("should show a form with a CheckBox") {
      val form = new ValidCheckBox() with TableBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      form.render should equal("<form action=\"\" enctype=\"application/x-www-form-urlencoded\" method=\"POST\"><tr><td><label for=\"id_firstname\">firstname:</label><input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input></td></tr><tr><td><label for=\"id_lastname\">lastname:</label><input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input></td></tr><tr><td><label for=\"id_people\">people:</label><input name=\"people\" type=\"checkbox\" value=\"Name\"></input></td></tr></form>")
    }


  }

}
