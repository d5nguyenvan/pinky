package org.pinky.guice

import com.google.inject.servlet.ServletModule
import scala.reflect.Manifest
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import com.google.inject.Key

abstract class ScalaModule extends com.google.inject.AbstractModule with uk.me.lings.scalaguice.ScalaModule


class ScalaServletModule extends ServletModule {

  protected def binderAccess = super.binder 

  def serveWith[T <: HttpServlet](url: String, urls: String*)(implicit m:Manifest[T]) { 
    super.serve(url,urls: _*).by(m.erasure.asInstanceOf[Class[T]])
  }
  def serveRegexWith[T <: HttpServlet](url: String, urls: String*)(implicit m:Manifest[T]) { 
    super.serveRegex(url,urls: _*).by(m.erasure.asInstanceOf[Class[T]])
  }
  def filterThrough[T <: Filter](url: String, urls: String*)(implicit m:Manifest[T]) {
    super.filter(url,urls : _*).through(m.erasure.asInstanceOf[Class[T]])
  } 
  def filterRegexThrough[T <: Filter](url: String, urls: String*)(implicit m:Manifest[T]) {
    super.filterRegex(url,urls: _*).through(m.erasure.asInstanceOf[Class[T]])
  }
  
}
// vim: set ts=4 sw=4 et:
