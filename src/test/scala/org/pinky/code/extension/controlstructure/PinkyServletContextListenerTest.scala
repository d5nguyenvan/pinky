package org.pinky.code.extension.controlstructure
import _root_.org.junit.Test
import com.google.inject.servlet.ServletModule
import com.google.inject.{AbstractModule, Guice, Injector}
import org.mockito.Mockito._
import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

/**
 * Created by IntelliJ IDEA.
 * User: phausel
 * Date: Jan 21, 2009
 * Time: 2:09:37 PM
 * To change this template use File | Settings | File Templates.
 */

class PinkyServletContextListenerTest  {

  @Test {val expected=classOf[java.lang.NullPointerException]}
  def should_fail_since_modules_are_not_populated() {
     val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
     }
     }
     val i = f.getInjectorPublic
     throw new Exception ("it should fail by now")
  }
   @Test 
  def should_fail_pass() {
     val f = new PinkyServletContextListener() {
      def getInjectorPublic(): Injector = {
        super.getInjector
     }
     }
     f.modules=Array(new ServletModule(){})
     val i = f.getInjectorPublic
     assertThat (i.getClass.getName, is("com.google.inject.InjectorImpl"))
  }
}

