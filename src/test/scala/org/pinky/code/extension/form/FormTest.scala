package org.pinky.code.extension.form

import annotation.form._
import com.jteigen.scalatest.JUnit4Runner
import hibernate.validator.Length
import javax.servlet.http.HttpServletRequest
import junit.runner.RunWith
import scalatest.matchers.ShouldMatchers
import scalatest.Spec
import org.pinky.code.extension.form.builder._
import scala.collection.mutable.Map
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

  class ValidFormRadioButton extends Form   {
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
    
    it ("should work with incoming request parameter Map"){
       val requestParams:scala.collection.jcl.Map[String,Array[String]] = new scala.collection.jcl.HashMap()
       requestParams.put("drop",Array("ko"))
       requestParams.put("firstname",Array("jon"))
       val form = new ValidDropDown() with TableBuilder
       form.loadRequest(requestParams) 
       form.drop should equal (Map("ko"->true) )
       form.firstName should equal ("jon")
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
      form.render should include ("<input value=\"yeah\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"lastname\" id=\"id_lastname\"></input>")
      form.render should include ("<input value=\"lol\" maxlength=\"20\" type=\"text\" size=\"20\" name=\"firstname\" id=\"id_firstname\"></input>")
      form.render should include ("<input name=\"radiobutton\" type=\"radio\" value=\"Name\"></input>")
      form.render should include ("<tr><td>")
      form.render should include ("</td></tr>")
    }
    it("should show a form with a DropDown") {
      val form = new ValidDropDown() with TableBuilder
      form.firstName = "lol"
      form lastName = "yeah"
      //TODO:add new asserts
    }
    it("should show a form with a CheckBox") {
      val form = new ValidCheckBox() with TableBuilder
      form.firstName = "lol"
      form lastName = "yeah"
       //TODO:add new asserts
    }


  }

}
