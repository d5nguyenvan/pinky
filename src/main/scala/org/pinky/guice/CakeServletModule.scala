package org.pinky.guice

import com.google.inject.servlet.ServletModule
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import scala.reflect.Manifest
import com.google.inject.Key

trait CakeSerlvetModule extends ScalaServletModule {

  self: ServletModule =>

  override def serveWith[T <: HttpServlet : Manifest](url: String) {
    bind(new Key[T]).toInstance(createInstance[T])
    super.serveWith[T](url)
  }

  override def serveRegexWith[T <: HttpServlet : Manifest](url: String) {
    bind(new Key[T]).toInstance(createInstance[T])
    super.serveRegexWith[T](url)
  }
  
  override def filterThrough[T <: Filter : Manifest](url: String) {
    bind(new Key[T]).toInstance(createInstance[T])
    super.filterThrough[T](url)
  }

  override def filterRegexThrough[T <: Filter : Manifest](url: String) {
    bind(new Key[T]).toInstance(createInstance[T])
    super.filterRegexThrough[T](url)
  }

  private def createInstance[T](implicit m: Manifest[T]):T = {
    if (m.erasure.getConstructors.size > 1) throw new InstantiationException("Type:"+m.erasure.toString + " has too many constructors")
    if (m.erasure.getConstructors()(0).getParameterTypes.size > 0) throw new InstantiationException("Type:"+m.erasure.toString + " should not have any constructor parameters")
    m.erasure.getConstructors()(0).newInstance(Seq()).asInstanceOf[T]
  }

}
// vim: set ts=4 sw=4 et:
