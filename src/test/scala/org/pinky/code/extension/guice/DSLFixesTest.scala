package org.pinky.code.extension.controlstructure.guice

import _root_.org.junit.Test
import com.google.inject.servlet.ServletModule
import com.google.inject.{AbstractModule, Guice, Injector}
import org.mockito.Mockito._
import org.scalatest.Spec
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

import _root_.scala.collection.jcl.HashMap

/**
 *  DSLFixes module tests
 *
 * @author fede silva gmail com   { fedesilva }
 */

@RunWith(classOf[JUnit4Runner])
class DSLFixesTest extends Spec with ShouldMatchers {

  /**
   *   If the module is not imported this will not even compile
   */

  describe("A DSL workaround") {
    it("should_compile_ok_by_servlet") {

      import org.pinky.code.extension.guice.DSLFixes._

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }
      f.modules = Array(new ServletModule() {
        serve("/hello/*").by(classOf[MockServlet])
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.InjectorImpl")


    }

    /**
     *   If the module is not imported this will not even compile
     */

    it("should_compile_ok_by_servlet_map") {

      import org.pinky.code.extension.guice.DSLFixes._

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }

      val params = new java.util.HashMap[String, String]();

      f.modules = Array(new ServletModule() {
        serve("/hello/*").by(classOf[MockServlet], params)
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.InjectorImpl")

    }

    /**
     *   If the module is not imported this will not even compile
     */
    it("should_compile_ok_withClass_servlet") {

      import org.pinky.code.extension.guice.DSLFixes._

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }
      f.modules = Array(new ServletModule() {
        serve("/hello/*").withClass(classOf[MockServlet])
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.InjectorImpl")


    }

    /**
     *   If the module is not imported this will not even compile
     */

    it("should_compile_ok_withClass_servlet_map") {

      import org.pinky.code.extension.guice.DSLFixes._

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }

      val params = new java.util.HashMap[String, String]();

      f.modules = Array(new ServletModule() {
        serve("/hello/*").withClass(classOf[MockServlet], params)
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.InjectorImpl")

    }

  }

}


