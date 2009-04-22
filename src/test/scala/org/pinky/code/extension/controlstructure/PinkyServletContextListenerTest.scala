package org.pinky.code.extension.controlstructure

import _root_.org.junit.Test
import com.google.inject.servlet.ServletModule
import com.google.inject.{AbstractModule, Guice, Injector}
import org.mockito.Mockito._
import org.scalatest.Spec
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:09:37 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(classOf[JUnit4Runner])
class PinkyServletContextListenerTest extends Spec with ShouldMatchers {
  describe("A Servlet Context Listener") {

    it("should_fail_since_modules_are_not_populated") {
      var exceptionIsThrown = false
      try {
        val f = new PinkyServletContextListener() {
          def getInjectorPublic(): Injector = {
            super.getInjector
          }
        }
        val i = f.getInjectorPublic
        i.hashCode()
      } catch {
          case ex: NullPointerException => exceptionIsThrown = true
          case _ =>
      }
      exceptionIsThrown should be (true)
    }

    it("should_fail_pass") {
      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }
      f.modules = Array(new ServletModule() {})
      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.InjectorImpl")
    }
  }
}

