package org.pinky.guice

import com.google.inject.servlet.ServletModule
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import scala.reflect.Manifest
import com.google.inject.Key

trait Cake extends ScalaServletModule {


  override def serveWith[T <: HttpServlet](url: String, urls: String*)(implicit m:Manifest[T]) {
    binderAccess.bind(m.erasure.asInstanceOf[Class[T]]).toInstance(createInstance[T])
    super.serveWith[T](url, urls:_*)
  }

  override def serveRegexWith[T <: HttpServlet](url: String, urls: String*)(implicit m:Manifest[T]) {
    binderAccess.bind(m.erasure.asInstanceOf[Class[T]]).toInstance(createInstance[T])
    super.serveRegexWith[T](url, urls:_*)
  }
  
  override def filterThrough[T <: Filter](url: String, urls: String*)(implicit m:Manifest[T]) {
    binderAccess.bind(m.erasure.asInstanceOf[Class[T]]).toInstance(createInstance[T])
    super.filterThrough[T](url, urls:_*)
  }

  override def filterRegexThrough[T <: Filter](url: String, urls: String*)(implicit m:Manifest[T]) {
    binderAccess.bind(m.erasure.asInstanceOf[Class[T]]).toInstance(createInstance[T])
    super.filterRegexThrough[T](url,urls:_*)
  }

  private def createInstance[T](implicit m: Manifest[T]):T = {
    if (m.erasure.getConstructors.size > 1) throw new InstantiationException("Type:"+m.erasure.toString + " has too many constructors")
    if (m.erasure.getConstructors()(0).getParameterTypes.size > 0) throw new InstantiationException("Type:"+m.erasure.toString + " should not have any constructor parameters")
    m.erasure.getConstructors()(0).newInstance(Seq()).asInstanceOf[T]
  }

}
// vim: set ts=4 sw=4 et:
