package org.pinky.form


import collection.mutable.Map

import org.pinky.form.builder._


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.pinky.annotation.form.{CheckBox, DropDown, RadioButton}
import net.sf.oval.constraint.Length


/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jul 2, 2009
 * Time: 11:54:23 AM
 * To change this template use File | Settings | File Templates.
 */


class FormTest extends Spec with ShouldMatchers {
  class NewForm extends Form {
    @RadioButton
    var radioButton: Map[String, Boolean] = _

    @Length(max = 20)
    var firstName: String = _

    @Length (max = 20)
    var lastName: String = _
  }

  class ValidFormRadioButton extends Form {
    @RadioButton
    var radioButton: Map[String, Boolean] = Map("name" -> false)

    @Length (max = 20)
    var firstName: String = _

    @Length (max = 20)
    var lastName: String = _
  }

  class ValidDropDown extends Form {
    @DropDown (multi = false)
    var drop: Map[String, Boolean] = Map("ko" -> false)

    @Length (max = 20)
    var firstName: String = _

    @Length (max = 20)
    var lastName: String = _
  }
  class ValidCheckBox extends Form {
    @CheckBox (multi = false)
    var people: Map[String, Boolean] = Map("name" -> false,"Jon"->true)

    @Length (max = 20)
    var firstName: String = _

    @Length (max = 20)
    var lastName: String = _
  }
  class NoNValidForm extends Form {
    @CheckBox (multi = false)
    var people: Map[String, Boolean] = Map("name" -> false,"Jon"->false)
  }

  describe("a Form") {

    it("should work with incoming request parameter Map") {
      val requestParams = new java.util.HashMap[String, Array[String]]()
      requestParams.put("drop", Array("ko"))
      requestParams.put("firstname", Array("jon"))
      val form = new ValidDropDown() with TableBuilder
      form.loadRequest(requestParams)
      form.drop should equal(Map("ko" -> true))
      form.firstName should equal("jon")
    }
    it("should complain about empty radioButton") {
      val form = new NewForm with TableBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      try {
        form.render
      } catch {
        case ex: Exception =>
        case _ => throw new Exception()
      }
    }
    it("should show a form with a Radiobutton") {
      val form = new ValidFormRadioButton() with TableBuilder
      form.firstName = "lol"
      form.lastName = "yeah"
      form.render should include("<input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input>")
      form.render should include("<input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input>")
      form.render should include("<input value=\"Name\" type=\"radio\" name=\"radiobutton\"></input>")
      form.render should include("<tr><td>")
      form.render should include("</td></tr>")
    }
    it("should show a form with a DropDown") {
      val form = new ValidDropDown() with ParagraphBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      form.render should include("<input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input>")
      form.render should include("<input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input>")
      form.render should include("<select name=\"drop\">")
      form.render should include("<option value=\"ko\">")
      form.render should not include("<tr><td>")
      form.render should include("<p>")
      form.render should include("</p>")
    }
    it("should show a form with a CheckBox") {
      val form = new ValidCheckBox() with UlTagBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      form.render should include("<input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input>")
      form.render should include("<input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input>")
      form.render should include ("<input value=\"Name\" type=\"checkbox\" name=\"people\"></input>")
      form.render should include ("<input value=\"Jon\" selected=\" \" type=\"checkbox\" name=\"people\"></input>")
      form.render should not include("<p>")
      form.render should include("<li>")
      form.render should include("</li>")
    }
     it ("should not validate an invalid form") {
      val form = new NoNValidForm() with UlTagBuilder with Validator
      println("class(invalid):"+this+" message:"+form.validate)
      form.validate.isEmpty should be (false)
     }

     it ("should validate a valid form") {
      val form = new ValidCheckBox() with UlTagBuilder with Validator
      println("class(valid):"+this+" message:"+form.validate)
      form.validate.isEmpty should be (true)
     }
  

  }

}
