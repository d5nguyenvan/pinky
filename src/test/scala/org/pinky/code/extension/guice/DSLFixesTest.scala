package org.pinky.code.extension.controlstructure.guice

import _root_.org.junit.Test
import com.google.inject.servlet.ServletModule
import com.google.inject.{AbstractModule, Guice, Injector}
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

import _root_.scala.collection.jcl.HashMap

/**
 *  DSLFixes module tests
 *
 *  @author fede silva gmail com {fedesilva}
 */

class DSLFixesTest  {

  /**
   *   If the module is not imported this will not even compile
   */
  @Test
  def should_compile_ok_by_servlet  {

    import org.pinky.code.extension.guice.DSLFixes._

    val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
      }
    }
    f.modules=Array(new ServletModule(){
        serve("/hello/*").by(classOf[MockServlet])
      });

    val i = f.getInjectorPublic
    assertThat (i.getClass.getName, is("com.google.inject.InjectorImpl"))


  }

  /**
   *   If the module is not imported this will not even compile
   */
  @Test
  def should_compile_ok_by_servlet_map  {

    import org.pinky.code.extension.guice.DSLFixes._

    val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
      }
    }

    val params = new java.util.HashMap[String, String]();

    f.modules=Array(new ServletModule(){
        serve("/hello/*").by(classOf[MockServlet], params)
      });

    val i = f.getInjectorPublic
    assertThat (i.getClass.getName, is("com.google.inject.InjectorImpl"))

  }

  /**
   *   If the module is not imported this will not even compile
   */
  @Test
  def should_compile_ok_withClass_servlet  {

    import org.pinky.code.extension.guice.DSLFixes._

    val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
      }
    }
    f.modules=Array(new ServletModule(){
        serve("/hello/*").withClass(classOf[MockServlet])
    });

    val i = f.getInjectorPublic
    assertThat (i.getClass.getName, is("com.google.inject.InjectorImpl"))


  }

  /**
   *   If the module is not imported this will not even compile
   */
  @Test
  def should_compile_ok_withClass_servlet_map  {

    import org.pinky.code.extension.guice.DSLFixes._

    val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
      }
    }

    val params = new java.util.HashMap[String, String]();

    f.modules=Array(new ServletModule(){
        serve("/hello/*").withClass(classOf[MockServlet], params)
    });

    val i = f.getInjectorPublic
    assertThat (i.getClass.getName, is("com.google.inject.InjectorImpl"))

  }


}


