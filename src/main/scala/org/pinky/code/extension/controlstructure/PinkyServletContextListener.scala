package org.pinky.code.extension.controlstructure


import _root_.scala.collection.jcl.ArrayList
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
class PinkyServletContextListener extends GuiceServletContextListener {

  var modules:Array[Module]=_

  /**
  * @return Injector
  * creates a guice injector from modules passed in via modules Array, without the array
  * this thing is not functioning
  * 
  *
  */
  override protected def getInjector(): Injector = {
    Guice.createInjector(modules:_*)
  }
}