package org.pinky.guice

import com.google.inject.servlet.ServletModule
import scala.reflect.Manifest
import javax.servlet.http.HttpServlet
import javax.servlet.Filter
import com.google.inject.Key
import org.pinky.util.AsClass._


class CakeServletModule extends ScalaServletModule {

  def bindServlet[T](servlet: HttpServlet): Builder[T] = {
    if (binderAccess == null) throw new RuntimeException("you can call this method only from servletConfig method")
    binderAccess.bind(servlet.asClass).toInstance(servlet)
    new CakeServletBuilder(this, servlet)
  }
  def bindFilter[T](filter: Filter): Builder[T] = {
    if (binderAccess == null) throw new RuntimeException("you can call this method only from servletConfig method")
    binderAccess.bind(filter.asClass).toInstance(filter)
    new CakeFilterBuilder(this, filter)
  }
  
}

class CakeFilterBuilder[T](module: ScalaServletModule, filter: Filter) extends Builder[T] {
  def toUrl(pattern: String, patterns: String*) {
     module._filter(pattern, patterns: _*).through(filter.asClass)
  }
  def toRegexUrl(pattern: String, patterns: String*){
     module._filterRegex(pattern, patterns: _*).through(filter.asClass)
  }

}
class CakeServletBuilder[T](module: ScalaServletModule, servlet: HttpServlet) extends Builder[T] {
  def toUrl(pattern: String, patterns: String*) {
     module._serve(pattern, patterns: _*).by(servlet.asClass)
  }
  def toRegexUrl(pattern: String, patterns: String*){
     module._serveRegex(pattern, patterns: _*).by(servlet.asClass)
  }
}

// vim: set ts=4 sw=4 et:
