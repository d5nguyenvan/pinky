package org.pinky.guice

import com.google.inject.Injector
import com.google.inject.servlet.GuiceServletContextListener
import com.google.inject.Module
import com.google.inject.Guice

/**
 * adds varargs support to guice injector creator,
 * also hides guice form the listener
 *
 * @author peter hausel gmail com (Peter Hausel)
 */
abstract class PinkyServletContextListener extends GuiceServletContextListener {

  implicit def builderToRichBuilder(
            builder: com.google.inject.servlet.ServletModule.ServletKeyBindingBuilder) = new DSLFixes.RichBuilder(builder);

  var modules: Array[Module] = _


  /**
   * @return Injector
   * creates a guice injector from modules passed in via modules Array, without the array
   * this thing is not functioning
   *
   *
   */
  override protected def getInjector(): Injector = {
    Guice.createInjector(modules: _*)
  }
}
