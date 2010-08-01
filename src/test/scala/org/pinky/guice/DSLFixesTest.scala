package org.pinky.guice

import com.google.inject.servlet.ServletModule
import com.google.inject.Injector
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

/**
 *  DSLFixes module tests
 *
 * @author fede silva gmail com   { fedesilva }
 */


class DSLFixesTest extends Spec with ShouldMatchers {

  /**
   *   If the module is not imported this will not even compile
   */

  describe("A DSL workaround") {
    it("should_compile_ok_by_servlet") {

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }
      f.modules = Array(new ServletModule() {
        serve("/hello/*").by(classOf[MockServlet])
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.internal.InjectorImpl")


    }

    /**
     *   If the module is not imported this will not even compile
     */

    it("should_compile_ok_by_servlet_map") {

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
      i.getClass.getName should equal("com.google.inject.internal.InjectorImpl")

    }

    /**
     *   If the module is not imported this will not even compile
     */
    it("should_compile_ok_withClass_servlet") {


      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }
      import org.pinky.guice.DSLFixes._
      f.modules = Array(new ServletModule() {

        serve("/hello/*").withClass(classOf[MockServlet])
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.internal.InjectorImpl")


    }

    /**
     *   If the module is not imported this will not even compile
     */

    it("should_compile_ok_withClass_servlet_map") {

      val f = new PinkyServletContextListener() {
        def getInjectorPublic(): Injector = {
          super.getInjector
        }
      }

      val params = new java.util.HashMap[String, String]();
      import org.pinky.guice.DSLFixes._
      f.modules = Array(new ServletModule() {
        serve("/hello/*").withClass(classOf[MockServlet], params)
      });

      val i = f.getInjectorPublic
      i.getClass.getName should equal("com.google.inject.internal.InjectorImpl")

    }

  }

}


