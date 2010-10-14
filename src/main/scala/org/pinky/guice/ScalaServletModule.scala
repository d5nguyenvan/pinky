package org.pinky.guice

import com.google.inject.servlet.ServletModule
import scala.reflect.Manifest
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import com.google.inject.Key

abstract class ScalaModule extends com.google.inject.AbstractModule with uk.me.lings.scalaguice.ScalaModule


class ScalaServletModule extends ServletModule {


  protected def binderAccess = super.binder 

  private[guice] def _serve(url:String, urls:String*)  = super.serve(url, urls:_*)
  private[guice] def _filter(url:String, urls:String*) = super.filter(url, urls:_*) 
  private[guice] def _serveRegex(url:String, urls:String*) = super.serveRegex (url, urls:_*)
  private[guice] def _filterRegex(url:String, urls:String*) = super.filterRegex (url, urls:_*)

  def bindServlet[T <: HttpServlet] (implicit m:Manifest[T]): Builder[T] = { 
    new ServletBuilder[T](this)
  }
  def bindFilter[T <: Filter](implicit m:Manifest[T]): Builder[T] = { 
    new FilterBuilder[T](this)
  }

}

abstract class Builder[T]() {
  def toUrl(pattern: String, patterns: String*)
  def toRegexUrl(pattern: String, patterns: String*)
}

class ServletBuilder[T <: HttpServlet](module: ScalaServletModule)(implicit m:Manifest[T]) extends Builder[T] {
  def toUrl(pattern: String, patterns: String*) {
     module._serve(pattern, patterns: _*).`with`((m.erasure.asInstanceOf[Class[T]]))
  }
  def toRegexUrl(pattern: String, patterns: String*){
     module._serveRegex(pattern, patterns: _*).`with`((m.erasure.asInstanceOf[Class[T]]))
  }
}

class FilterBuilder[T <: Filter](module: ScalaServletModule)(implicit m:Manifest[T]) extends Builder[T] {
  def toUrl(pattern: String, patterns: String*){
     module._filter(pattern, patterns.toArray: _*).through((m.erasure.asInstanceOf[Class[T]]))
  }
  def toRegexUrl(pattern: String, patterns: String*){
     module._filterRegex(pattern, patterns.toArray: _*).through((m.erasure.asInstanceOf[Class[T]]))
  }
}
// vim: set ts=4 sw=4 et:
